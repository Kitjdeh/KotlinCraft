package com.ncc.kotlincraft

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import java.util.Stack
import kotlin.math.round
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    //expression : 중위 표현식
    private var expression = ""

    //후위 표현식
    private val resultStack = Stack<Double>()

    //후위 표현식
    private val postFixStack = Stack<String>()

    private val listOrder = mutableListOf<String>()

    var totalNumber = ""
    val stack = Stack<String>()
    var num = ""
    val value = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val test = "test1"
        val test2 = "test2"
        val test3 = "test3"

        val oneBtn = findViewById<TextView>(R.id.oneBtn)
        val twoBtn = findViewById<TextView>(R.id.twoBtn)
        val threeBtn = findViewById<TextView>(R.id.threeBtn)
        val fourBtn = findViewById<TextView>(R.id.fourBtn)
        val fiveBtn = findViewById<TextView>(R.id.fiveBtn)
        val sixBtn = findViewById<TextView>(R.id.sixBtn)
        val sevenBtn = findViewById<TextView>(R.id.sevenBtn)
        val eightBtn = findViewById<TextView>(R.id.eightBtn)
        val nineBtn = findViewById<TextView>(R.id.nineBtn)
        val zeroBtn = findViewById<TextView>(R.id.zeroBtn)
        val plusBtn = findViewById<TextView>(R.id.plusBtn)
        val minusBtn = findViewById<TextView>(R.id.minusBtn)
        val multiplyBtn = findViewById<TextView>(R.id.multiplyBtn)
        val divideBtn = findViewById<TextView>(R.id.divideBtn)
        val clearBtn = findViewById<TextView>(R.id.clearBtn)
        val deleteBtn = findViewById<TextView>(R.id.deletBtn)
        val calculatorBtn = findViewById<TextView>(R.id.equalBtn)
        val leftParenthesis = findViewById<TextView>(R.id.left_parenthesis)
        val rightParenthesis = findViewById<TextView>(R.id.right_parenthesis)
        val result = findViewById<TextView>(R.id.result)

        oneBtn.setOnClickListener {
            expression += "1"
            num += "1"
            result.text = expression
        }
        twoBtn.setOnClickListener {
            expression += "2"
            num += "2"
            result.text = expression
        }
        threeBtn.setOnClickListener {
            expression += "3"
            num += "3"
            result.text = expression
        }
        fourBtn.setOnClickListener {
            expression += "4"
            num += "4"
            result.text = expression
        }
        fiveBtn.setOnClickListener {
            expression += "5"
            num += "5"
            result.text = expression
        }
        sixBtn.setOnClickListener {
            expression += "6"
            num += "6"
            result.text = expression
        }
        sevenBtn.setOnClickListener {
            expression += "7"
            num += "7"
            result.text = expression
        }
        eightBtn.setOnClickListener {
            expression += "8"
            num += "8"
            result.text = expression
        }
        nineBtn.setOnClickListener {
            expression += "9"
            num += "9"
            result.text = expression
        }
        zeroBtn.setOnClickListener {
            expression += "0"
            num += "0"
            result.text = expression
        }

        deleteBtn.setOnClickListener {
            expression = expression.substring(0, -1)
            result.text = expression
        }
        clearBtn.setOnClickListener {
            expression = ""
            num = ""
            result.text = expression
            listOrder.clear()
            resultStack.clear()
            postFixStack.clear()
        }
        plusBtn.setOnClickListener {
            expression += "+"
//            if (num.isNotEmpty()) {
//                listOrder.add(num)
//            }
//            listOrder.add("+")
            result.text = expression
            num = ""
        }
        minusBtn.setOnClickListener {
            expression += "-"
//            if (num.isNotEmpty()) {
//                listOrder.add(num)
//            }
//            listOrder.add("-")
            result.text = expression
            num = ""
        }
        multiplyBtn.setOnClickListener {
            expression += "*"
//            if (num.isNotEmpty()) {
//                listOrder.add(num)
//            }
//            listOrder.add("*")
            result.text = expression
            num = ""
        }
        divideBtn.setOnClickListener {
            expression += "/"
//            if (num.isNotEmpty()) {
//                listOrder.add(num)
//            }
//            listOrder.add("/")
            result.text = expression
            num = ""
        }
        leftParenthesis.setOnClickListener {
            expression += "("
//            listOrder.add("(")
//            result.text = expression
//            num = ""
        }
        rightParenthesis.setOnClickListener {
            expression += ")"
//            listOrder.add(num)
//            listOrder.add(")")
//            num = ""
            result.text = expression
        }

        calculatorBtn.setOnClickListener {
            expression = ""
            postFix()
            result.text = expression
        }
    }

    private fun getResult() {

    }

    //중위 표현식 expression을 후위표현식으로 변환
    private fun postFix() {
        // 2자리수 이상의 string 일경우 words에 포함시켜서 진행
        var words = ""
        for (word in expression) {
            if (word.isDigit() || word.equals(".")) {
                //.이 이미 있다면 break
                if (words.contains(".")) {
                    Toast.makeText(this, "잘못된 수식입니다.", Toast.LENGTH_SHORT).show()
                    break
                }
                words += word
            } else {
                //isDigt가 아니다 -> 연산 or ()이니 계산된 words 를 넣어준다.
                if (words.isNotEmpty()) {
                    stack.add(words)
                    words = ""
                }
                if (word.equals("(")) {
                    stack.add(word.toString())
                }

                // ")" 가 나올 경우 해당 )과 맞아 떨어지는 (이 나오기 전까지 stack들을 postFixStack에 집어넣는다.(앞자리로 보내서 먼저 계산 할 수 있도록 만든다.)
                else if (word.equals(")")) {
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        postFixStack.add(stack.pop())
                    }
                    // while 이 종료되었다면 stack의 맨뒤가 ( 이거나 stack이 비어있음 -> pop()로 정리
                    stack.pop()
                } else if (word.equals("*") || word.equals("/")) {

                    // *과 / 가 나올경우 이전에 나온 *나 /가 먼저기 때문에 해당 값들을 후위 표현식에 넣어준다.
                    while (stack.isNotEmpty() && (stack.last() == "*" || stack.last() == "/")) {
                        postFixStack.add(stack.pop())
                    }
                    stack.add(word.toString())
                } else if (word.equals("+") || word.equals("-")) {
                    // +와 - 는 우선순이가 제일 낮기 때문에 () 기준으로 가장 뒤에 가도록 후위 표현식에 배치한다.
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        postFixStack.add(stack.pop())
                    }
                    stack.add(word.toString())
                }
            }
            while (stack.isNotEmpty()) {
                postFixStack.add(stack.pop())
            }
            calculateStack()
        }
    }

    private fun calculateStack() {
        loop@ for (num in postFixStack) {
            if (num.isDigitsOnly()) {
                resultStack.add(num.toDouble())
            } else if (num == "+") {
                val secondNum = resultStack.pop()
                val firstNum = resultStack.pop()
                val answer = firstNum + secondNum
                resultStack.add(answer)
            } else if (num == "-") {
                val secondNum = resultStack.pop()
                val firstNum = resultStack.pop()
                val answer = firstNum - secondNum
                resultStack.add(answer)
            } else if (num == "*") {
                val secondNum = resultStack.pop()
                val firstNum = resultStack.pop()
                val answer = firstNum * secondNum
                resultStack.add(answer)
            } else if (num == "/") {
                val secondNum = resultStack.pop()
                val firstNum = resultStack.pop()
                if ((round(secondNum * 1000) / 1000).roundToInt() == 0) {
                    expression = "에러 : 분모가 0입니다."
                    break@loop
                }
                val answer = firstNum / secondNum
                resultStack.add(answer)
            }
            Log.d("postFixStack", postFixStack.toString())
            Log.d("계산중", resultStack.toString())
        }
        if (expression != "에러 : 분모가 0입니다.") {
            expression = resultStack.pop().toString()
        }
    }
}