package com.ncc.kotlincraft.presentation.view.main

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncc.kotlincraft.R
import com.ncc.kotlincraft.data.db.RecordDatabase
import com.ncc.kotlincraft.data.db.entity.Record
import com.ncc.kotlincraft.domain.model.DomainRecord
import com.ncc.kotlincraft.domain.usecase.RecordUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Stack
import kotlin.math.round
import kotlin.math.roundToInt

class MainViewModel : ViewModel() {

    var db: RecordDatabase? = null

    //recordUsecase 인스턴스 생성
    private val recordUseCase = RecordUseCase()

    //외부에서 입력하게 될 경우 멀티 스레드 환경에서 일정한 값을 유지 할 수 없기 때문에 private선언 후 세터로 접근
    //뷰 모델에서 사용할 게터와 세터가 모두 있는 _expression
    private val _expression = MutableLiveData<String>("")

    //뷰에서 혹시 건드리지 못하도록 게터만 넣은 expression
    val expression: LiveData<String>
        get() = _expression

    //수식에 문제가 있을 경우 알려주는 라이브 데이터 warning
    private val _warning = MutableLiveData<String>("")

    val warning: LiveData<String>
        get() = _warning

    //후위 표현식
    private val resultStack = Stack<Double>()

    //후위 표현식
    private val postFixStack = Stack<String>()

    //연산 용 stack
    private val stack = Stack<String>()

    //String 추가
    fun addExpression(string: String) {
        val newExpression = _expression.value + string
        _expression.postValue(newExpression)
    }

    fun deleteExpression() {
        val n = expression.value!!.length
        if (n > 0) {
            val newExpression = expression.value!!.substring(0, n - 1)
            _expression.postValue(newExpression)
        }
    }

    //함수를 연산하는 calculate를 실행하고 error가 없으면 해당 값을 write에 전달
    fun writeExpression() {
        val first = _expression.value.toString()
        //결과 값이 error 인 경우 warning 라이브 데이터에 전달, else인 경우 epression에 전달
        when (val result = recordUseCase.calculate(first)) {
            "value_error" ->
                _warning.value = "value_error"

            "value_zero" ->
                _warning.value = "value_zero"

            else -> {
                viewModelScope.launch {
                    val record = DomainRecord(id = null, expression = "$first=$result")
                    _expression.postValue(result)
                    //뷰모델스코프는 기본적으로 메인스레드에서 작동하기 때문에 백그라운드 스레드로 변경
                    withContext(Dispatchers.IO) {
                        recordUseCase.writeRecord(record)
                    }
                }
            }
        }
    }

    fun clear() {
        _expression.value = ""
        resultStack.clear()
        postFixStack.clear()
//        _saveExpression.value = ""
    }
}