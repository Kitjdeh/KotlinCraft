package com.ncc.kotlincraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var expression = "0"

    val listNumber = mutableListOf<Int>(expression.toInt())
    val listOrder = mutableListOf<String>()
    var totalNumber = ""
    val value = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            result.text = expression
        }
        twoBtn.setOnClickListener {
            expression += "2"
            result.text = expression
        }
        threeBtn.setOnClickListener {
            expression += "3"
            result.text = expression
        }
        fourBtn.setOnClickListener {
            expression += "4"
            result.text = expression
        }
        fiveBtn.setOnClickListener {
            expression += "5"
            result.text = expression
        }
        sixBtn.setOnClickListener {
            expression += "6"
            result.text = expression
        }
        sevenBtn.setOnClickListener {
            expression += "7"
            result.text = expression
        }
        eightBtn.setOnClickListener {
            expression += "8"
            result.text = expression
        }
        nineBtn.setOnClickListener {
            expression += "9"
            result.text = expression
        }
        zeroBtn.setOnClickListener {
            expression += "0"
            result.text = expression
        }
        clearBtn.setOnClickListener {
            expression = "0"
            result.text = expression
        }

        plusBtn.setOnClickListener {
            expression +="+"
            result.text = expression
        }
        minusBtn.setOnClickListener {
            expression +="-"
            result.text = expression

        }
        multiplyBtn.setOnClickListener {
            expression +="-"
            result.text = expression

        }
        leftParenthesis.setOnClickListener {
            expression +="("
            result.text = expression
        }
        rightParenthesis.setOnClickListener {
            expression +=")"
            result.text = expression
        }
    }

    private fun getResult() {

    }
}