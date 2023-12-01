package com.ncc.kotlincraft.presentation.view.main

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncc.kotlincraft.data.db.RecordDatabase
import com.ncc.kotlincraft.data.db.entity.Record
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Stack
import kotlin.math.round
import kotlin.math.roundToInt

class MainViewModel : ViewModel() {

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

    private val _saveExpression = MutableLiveData<String>("")
    val saveExpression: LiveData<String>
        get() = _saveExpression

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

    //중위 배열식을 후위 배열식으로 변환
    fun postFix() {
        // 2자리수 이상의 string 일경우 words에 포함시켜서 진행
        var words = ""
        for (char in _expression.value.toString()) {
            val word = char.toString()
            if (word.isDigitsOnly() || word == ".") {
                //.이 이미 있다면 break
                if (words.contains(".") && word == ".") {
                    _warning.value = "value_error"
                    break
                }
                words += word
            } else {
                //isDigt가 아니다 -> 연산 or ()이니 계산된 words 를 넣어준다.
                if (words.isNotEmpty()) {
                    postFixStack.add(words)
                    words = ""
                }
                if (word == "(") {
                    stack.add(word)
                }

                // ")" 가 나올 경우 해당 )과 맞아 떨어지는 (이 나오기 전까지 stack들을 postFixStack에 집어넣는다.(앞자리로 보내서 먼저 계산 할 수 있도록 만든다.)
                else if (word == ")") {
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        postFixStack.add(stack.pop())
                    }
                    // while 이 종료되었다면 stack의 맨뒤가 ( 이거나 stack이 비어있음 -> pop()로 정리
                    stack.pop()
                } else if (word == "*" || word == "/") {
                    // *과 / 가 나올경우 이전에 나온 *나 /가 먼저기 때문에 해당 값들을 후위 표현식에 넣어준다.
                    while (stack.isNotEmpty() && (stack.last() == "*" || stack.last() == "/")) {
                        postFixStack.add(stack.pop())
                    }
                    stack.add(word)

                } else if (word == "+" || word == "-") {
                    // +와 - 는 우선순이가 제일 낮기 때문에 () 기준으로 가장 뒤에 가도록 후위 표현식에 배치한다.
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        postFixStack.add(stack.pop())
                    }
                    stack.add(word)
                }
            }
        }
        if (words.isNotEmpty()) {
            postFixStack.add(words)
        }
        while (stack.isNotEmpty()) {
            postFixStack.add(stack.pop())
        }
        calculateStack()
    }

    private fun calculateStack() {
        loop@ for (num in postFixStack) {
            if (num.isDigitsOnly() || num.contains(".")) {
                resultStack.add(num.toDouble())
            } else {
                if (num.isDigitsOnly() || resultStack.size < 2) {
                    _warning.value = "value_error"
                    break@loop
                }
                val secondNum = resultStack.pop()
                val firstNum = resultStack.pop()
                if (num == "+") {
                    val answer = firstNum + secondNum
                    resultStack.add(answer)
                } else if (num == "-") {
                    val answer = firstNum - secondNum
                    resultStack.add(answer)
                } else if (num == "*") {
                    val answer = firstNum * secondNum
                    resultStack.add(answer)
                } else if (num == "/") {
                    if (secondNum.equals(0.0)) {
                        _warning.value = "value_zero"
                        break@loop
                    } else {
                        if ((round(secondNum * 1000) / 1000).roundToInt() == 0) {
                            _warning.value = "value_zero"
                            break@loop
                        }
                        val answer = firstNum / secondNum
                        resultStack.add(answer)
                    }
                }
            }
        }
        if (_warning.value == "") {
            val result = resultStack.pop().toString()
            _saveExpression.postValue(_expression.value + "=" + result)
            _expression.postValue(result)
            resultStack.clear()
            postFixStack.clear()
        }
//        if (_warning.value =="") {
////            //결과값 저장
////            val recordDao = db.recordDao()
//            var saveExpression = _expression.value.toString()
//            _expression.postValue(resultStack.pop().toString())
//
//            // DB에 접근 할 대 메인 쓰레드를 쓰면 에러가 나기 때문에 Dispathcer.io로 백그라운드 스레드에서 작업
//            CoroutineScope(Dispatchers.IO).launch {
//                saveExpression += "=$expression"
//                val record = Record(null, saveExpression)
//                db!!.recordDao().insertRecord(record)
//            }
//        }
    }


    fun clear() {
        _expression.value = ""
        resultStack.clear()
        postFixStack.clear()
        _saveExpression.value = ""
    }
}