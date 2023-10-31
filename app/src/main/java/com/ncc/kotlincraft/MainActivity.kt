package com.ncc.kotlincraft

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.text.isDigitsOnly
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Stack
import kotlin.math.round
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    //expression : 중위 표현식
    private var expression = ""

    //전달할 중위 표현식
    private var saveExpression = ""

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
        val pointBtn = findViewById<TextView>(R.id.pointBtn)
        val recordBtn = findViewById<AppCompatButton>(R.id.btn_mainToRecord)


        recordBtn.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }
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
        pointBtn.setOnClickListener {
            Log.d(expression.last().toString(), expression.last().isDigit().toString())
            Log.d(
                expression.last().toString(),
                expression.last().toString().isDigitsOnly().toString()
            )
            if (!expression.last().toString().isDigitsOnly()) {
                Log.d(expression.last().toString(), expression.last().isDigit().toString())
                Log.d(
                    expression.last().toString(),
                    expression.last().toString().isDigitsOnly().toString()
                )
                Toast.makeText(this, "잘못된 수식입니다.", Toast.LENGTH_SHORT).show()
            } else {
                expression += "."
                result.text = expression
            }
        }

        deleteBtn.setOnClickListener {
            var n = expression.length
            if (n > 0) {
                expression = expression.substring(0, n - 1)
            }

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
            saveExpression = expression
            postFix()
            result.text = expression
            if (expression.toDouble().toInt() > 0){
                when (val number = expression.toDouble().toInt() ) {
                    in 0..10 -> {
                        Log.d("0~10",number.toString())
                        result.setBackgroundColor(Color.YELLOW)
                    }
                    in 11..100 -> {
                        Log.d("11~100",number.toString())
                        result.setBackgroundColor(Color.GREEN)
                    }
                    in 101..1000 -> {
                        Log.d("101~1000",number.toString())
                        result.setBackgroundColor(Color.RED)
                    }
                    else -> {
                        Log.d("1001~",number.toString())
                        result.setBackgroundColor(Color.BLUE)
                    }
                }

            }

        }
    }

    private fun getResult() {

    }

    //중위 표현식 expression을 후위표현식으로 변환
    private fun postFix() {
        // 2자리수 이상의 string 일경우 words에 포함시켜서 진행
        var words = ""
        Log.d("expression", expression)
        for (char in expression) {
            val word = char.toString()
            if (word.isDigitsOnly() || word == ".") {
                //.이 이미 있다면 break
                if (words.contains(".") && word == ".") {
                    Toast.makeText(this, "잘못된 수식입니다.", Toast.LENGTH_SHORT).show()
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
                    stack.add(word.toString())
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
                    stack.add(word.toString())

                } else if (word == "+" || word == "-") {
                    // +와 - 는 우선순이가 제일 낮기 때문에 () 기준으로 가장 뒤에 가도록 후위 표현식에 배치한다.
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        postFixStack.add(stack.pop())
                    }
                    stack.add(word.toString())
                }
            }
        }
        if (words.isNotEmpty()) {
            postFixStack.add(words)
        }
        while (stack.isNotEmpty()) {
            postFixStack.add(stack.pop())
        }
        Log.d(postFixStack.toString(), "$stack")
        calculateStack()
    }

    private fun calculateStack() {
        //RoomDb인스턴스 생성
        val db = Room.databaseBuilder(
            applicationContext,
            RecordDatabase::class.java, "record"
        ).build()
        println("TEST TEST TEST DB1`:${db}")
        loop@ for (num in postFixStack) {
            if (num.isDigitsOnly() || num.contains(".")) {
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
                    expression = ""
                    break@loop
                }
                val answer = firstNum / secondNum
                resultStack.add(answer)
            }
//            Log.d("postFixStack", postFixStack.toString())
//            Log.d("resultStack", resultStack.toString())
        }
        if (expression != "에러 : 분모가 0입니다.") {
//            //결과값 저장
//            val recordDao = db.recordDao()


            expression = resultStack.pop().toString()

            // DB에 접근 할 대 메인 쓰레드를 쓰면 에러가 나기 때문에 Dispathcer.io로 백그라운드 스레드에서 작업
            CoroutineScope(Dispatchers.IO).launch {
                saveExpression += "=$expression"
                val record = Record(null, saveExpression)
                db.recordDao().insertRecord(record)
            }

        }
    }
}