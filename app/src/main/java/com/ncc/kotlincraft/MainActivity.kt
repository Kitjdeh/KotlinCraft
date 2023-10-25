package com.ncc.kotlincraft

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import java.util.Stack

class MainActivity : AppCompatActivity() {

    private var expression = ""
    private val resultStack = Stack<Int>()
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
        val plusMinusBtn = findViewById<TextView>(R.id.plus_minusBtn)
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
            if (num.isNotEmpty()) {
                listOrder.add(num)
            }
            listOrder.add("+")
            result.text = expression
            num = ""
        }
        minusBtn.setOnClickListener {
            expression += "-"
            if (num.isNotEmpty()) {
                listOrder.add(num)
            }
            listOrder.add("-")
            result.text = expression
            num = ""
        }
        multiplyBtn.setOnClickListener {
            expression += "*"
            if (num.isNotEmpty()) {
                listOrder.add(num)
            }
            listOrder.add("*")
            result.text = expression
            num = ""
        }
        divideBtn.setOnClickListener {
            expression += "/"
            if (num.isNotEmpty()) {
                listOrder.add(num)
            }
            listOrder.add("/")
            result.text = expression
            num = ""
        }
        leftParenthesis.setOnClickListener {
            expression += "("
            listOrder.add("(")
            result.text = expression
//            num = ""
        }
        rightParenthesis.setOnClickListener {
            expression += ")"
            listOrder.add(num)
            listOrder.add(")")
            num = ""
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

    private fun postFix() {
        Log.d("입력값", listOrder.toString())
        for (num in listOrder) {
            if (num.isDigitsOnly()) {
                postFixStack.add(num)
            } else if (num == "(") {
                stack.add(num)
            } else if (num == ")") {
                while (stack.isNotEmpty() && stack.last() != "(") {
                    postFixStack.add(stack.pop())
                }
                stack.pop()
            } else if (num == "*" || num == "/") {
                while (stack.isNotEmpty() && (stack.last() == "*" || stack.last() == "/")) {
                    postFixStack.add(stack.pop())
                }
                stack.add(num)
            } else if (num == "+" || num == "-") {
                while (stack.isNotEmpty() && stack.last() != "(") {
                    postFixStack.add(stack.pop())
                }
                stack.add(num)
            }
        }
        Log.d("listorder 순회 끝", stack.toString())
        while (stack.isNotEmpty()) {
            postFixStack.add(stack.pop())
        }
        calculateStack()
    }

    private fun calculateStack() {
        loop@ for (num in postFixStack) {
//        while (num in postFixStack){
            if (num.isDigitsOnly()) {
                resultStack.add(num.toInt())
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
                if (secondNum == 0) {
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