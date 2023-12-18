package com.ncc.kotlincraft.domain.usecase


import android.util.Log
import androidx.core.text.isDigitsOnly
import com.ncc.kotlincraft.data.entity.Record
import com.ncc.kotlincraft.data.repository.RecordRepositoryImpl
import com.ncc.kotlincraft.domain.model.DomainRecord
import com.ncc.kotlincraft.domain.repository.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Stack
import kotlin.math.round
import kotlin.math.roundToInt

//뷰 모델에서 바로 레퍼지토리를 연결하지 않고 usecase로 연결을 하여 수정이 일어나도 해당 부분만 수정하도록 설정
class RecordUseCase {
    private val repository = RecordRepositoryImpl()

    //연산 용 stack
    private val stack = Stack<String>()

    fun getRecord(): List<DomainRecord> {
        return repository.getRecords()
    }

    fun calculate(data: String): String = postFix(data)

    fun writeRecord(record: DomainRecord) = repository.writeRecord(record)

    fun deleteRecord(record: DomainRecord): List<DomainRecord>  = repository.deleteRecord(record)

    fun changeRecord(start: DomainRecord, end: DomainRecord) = repository.changeRecord(start, end)

    private fun postFix(expression: String): String {
        //후위 표현식
        val postFixStack = Stack<String>()
        // 2자리수 이상의 string 일경우 words에 포함시켜서 진행
        var words = ""
        for (char in expression) {
            val word = char.toString()
            if (word.isDigitsOnly() || word == ".") {
                //.이 이미 있다면 break
                if (words.contains(".") && word == ".") {
                    return "value_error"
                }
                words += word
            } else {
                //isDigt가 아니다 -> 연산 or ()이니 계산된 words 를 넣어준다.
                if (words.isNotEmpty()) {
                    postFixStack.add(words)
                    words = ""
                }
                when (word) {
                    "(" -> {
                        stack.add(word)
                    }
                    // ")" 가 나올 경우 해당 )과 맞아 떨어지는 (이 나오기 전까지 stack들을 postFixStack에 집어넣는다.(앞자리로 보내서 먼저 계산 할 수 있도록 만든다.)
                    ")" -> {
                        while (stack.isNotEmpty() && stack.last() != "(") {
                            postFixStack.add(stack.pop())
                        }
                        // while 이 종료되었다면 stack의 맨뒤가 ( 이거나 stack이 비어있음 -> pop()로 정리
                        stack.pop()
                    }
                    "*", "/" -> {
                        // *과 / 가 나올경우 이전에 나온 *나 /가 먼저기 때문에 해당 값들을 후위 표현식에 넣어준다.
                        while (stack.isNotEmpty() && (stack.last() == "*" || stack.last() == "/")) {
                            postFixStack.add(stack.pop())
                        }
                        stack.add(word)
                    }
                    "+", "-" -> {
                        // +와 - 는 우선순이가 제일 낮기 때문에 () 기준으로 가장 뒤에 가도록 후위 표현식에 배치한다.
                        while (stack.isNotEmpty() && stack.last() != "(") {
                            postFixStack.add(stack.pop())
                        }
                        stack.add(word)
                    }
                }
            }
        }
        if (words.isNotEmpty()) {
            postFixStack.add(words)
        }
        while (stack.isNotEmpty()) {
            postFixStack.add(stack.pop())
        }
        return calculateStack(postFixStack)
    }

    private fun calculateStack(postFixStack: Stack<String>): String {
        //후위 표현식 - 전역 변수가 아닌 calculateStack의 지역 변수로 호출
        val resultStack = Stack<Double>()
        for (num in postFixStack) {
            if (num.isDigitsOnly() || num.contains(".")) {
                resultStack.add(num.toDouble())
            } else {
                if (num.isDigitsOnly() || resultStack.size < 2) {
                    return "value_error"
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
                        return "value_zero"
                    } else {
                        if ((round(secondNum * 1000) / 1000).roundToInt() == 0) {
                            return "value_zero"
                        }
                        val answer = firstNum / secondNum
                        resultStack.add(answer)
                    }
                }
            }
        }
//        val result = resultStack.pop().toString()
        //연산 과정의 stack들이 함수 호출 시 자동 초기화 되기 때문에 clear 과정 생략
//        resultStack.clear()
//        postFixStack.clear()
//        CoroutineScope(Dispatchers.IO).launch {
//            val record = DomainRecord(id = null, expression = "$expression=$result")
//            repository.writeRecord(record)
//        }
        return resultStack.pop().toString()
    }
}