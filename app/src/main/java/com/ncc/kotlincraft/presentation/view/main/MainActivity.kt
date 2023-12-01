package com.ncc.kotlincraft.presentation.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import com.ncc.kotlincraft.R
import com.ncc.kotlincraft.data.db.entity.Record
import com.ncc.kotlincraft.data.db.RecordDatabase
import com.ncc.kotlincraft.presentation.view.record.RecordActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Stack

class MainActivity : AppCompatActivity() {

    private var db: RecordDatabase? = null

    //expression : 중위 표현식
    private var expression = ""

    //전달할 중위 표현식
//    private var saveExpression = ""
//
//    //후위 표현식
//    private val resultStack = Stack<Double>()
//
//    //후위 표현식
//    private val postFixStack = Stack<String>()
//
//    private val listOrder = mutableListOf<String>()

    var totalNumber = ""
    val stack = Stack<String>()
    val value = 0

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = RecordDatabase.getInstance(this)


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

        //뷰모델의 livedata를 observe 한다.
        viewModel.expression.observe(this) { expression ->
            result.text = expression
            this.expression = expression
        }
        //연산이 완료 된 경우 db에 저장을 명령하는 saveexpresssion observe
        viewModel.saveExpression.observe(this) { saveExpression ->
            // DB에 접근 할 대 메인 쓰레드를 쓰면 에러가 나기 때문에 Dispathcer.io로 백그라운드 스레드에서 작업
            if (saveExpression.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val record = Record(null, saveExpression)
                    db!!.recordDao().insertRecord(record)
                }
            }
        }
        //수식에 문제가 있을 경우 알려주는 warning observe
        viewModel.warning.observe(this) { warning ->
            when (warning) {
                "value_error" -> Toast.makeText(
                    this,
                    getString(R.string.value_error),
                    Toast.LENGTH_SHORT
                ).show()
                "value_zero" -> Toast.makeText(
                    this,
                    getString(R.string.value_zero),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        recordBtn.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }
        oneBtn.setOnClickListener {
//            expression += "1"
//            result.text = expression
            viewModel.addExpression("1")
        }
        twoBtn.setOnClickListener {
//            expression += "2"
//            result.text = expression
            viewModel.addExpression("2")
        }
        threeBtn.setOnClickListener {
//            expression += "3"
//            result.text = expression
            viewModel.addExpression("3")
        }
        fourBtn.setOnClickListener {
//            expression += "4"
//            result.text = expression
            viewModel.addExpression("4")
        }
        fiveBtn.setOnClickListener {
//            expression += "5"
//            result.text = expression
            viewModel.addExpression("5")
        }
        sixBtn.setOnClickListener {
//            expression += "6"
//            result.text = expression
            viewModel.addExpression("6")
        }
        sevenBtn.setOnClickListener {
//            expression += "7"
//            result.text = expression
            viewModel.addExpression("7")
        }
        eightBtn.setOnClickListener {
//            expression += "8"
//            result.text = expression
            viewModel.addExpression("8")
        }
        nineBtn.setOnClickListener {
//            expression += "9"
//            result.text = expression
            viewModel.addExpression("9")
        }
        zeroBtn.setOnClickListener {
//            expression += "0"
//            result.text = expression
            viewModel.addExpression("0")
        }
        pointBtn.setOnClickListener {
//            if (!expression.last().toString().isDigitsOnly()) {
//                expression != getString(R.string.value_error)
//                Toast.makeText(this, getString(R.string.value_error), Toast.LENGTH_SHORT).show()
//            } else {
//                expression += "."
//                result.text = expression
//            }
            viewModel.addExpression(".")
        }

        deleteBtn.setOnClickListener {
            viewModel.deleteExpression()
        }
        clearBtn.setOnClickListener {
            viewModel.clear()
//            expression = ""
//            result.text = expression
//            listOrder.clear()
//            resultStack.clear()
//            postFixStack.clear()
        }
        plusBtn.setOnClickListener {
//            expression += "+"
//            result.text = expression
            viewModel.addExpression("+")
        }
        minusBtn.setOnClickListener {
//            expression += "-"
//            result.text = expression
            viewModel.addExpression("-")
        }
        multiplyBtn.setOnClickListener {
//            expression += "*"
//            result.text = expression
            viewModel.addExpression("*")
        }
        divideBtn.setOnClickListener {
//            expression += "/"
//            result.text = expression
            viewModel.addExpression("/")
        }
        leftParenthesis.setOnClickListener {
            expression += "("
        }
        rightParenthesis.setOnClickListener {
            expression += ")"
            result.text = expression
        }
        calculatorBtn.setOnClickListener {
//            saveExpression = expression
            viewModel.postFix()
        }
    }
}