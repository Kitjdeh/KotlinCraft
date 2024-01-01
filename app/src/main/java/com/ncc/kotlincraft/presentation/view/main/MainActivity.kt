package com.ncc.kotlincraft.presentation.view.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import com.ncc.kotlincraft.R
import com.ncc.kotlincraft.data.db.entity.Record
import com.ncc.kotlincraft.data.db.RecordDatabase
import com.ncc.kotlincraft.databinding.ActivityMainBinding
import com.ncc.kotlincraft.presentation.view.record.RecordActivity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Stack

import kotlin.math.round
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    //expression : 중위 표현식
    private var expression = ""

    val value = 0

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        db = RecordDatabase.getInstance(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뷰모델의 livedata를 observe 한다.
        viewModel.expression.observe(this) { expression ->
            binding.result.text = expression
            this.expression = expression
        }

        //수식에 문제가 있을 경우 알려주는 warning observe
        viewModel.warning.observe(this) { warning ->
            when (warning) {
                "value_error" ->
                    showToastMessage(getString(R.string.value_error))

                "value_zero" ->
                    showToastMessage(getString(R.string.value_zero))
            }
        }
        binding.btnMainToRecord.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }
        binding.oneBtn.setOnClickListener {
            viewModel.addExpression("1")
        }
        binding.twoBtn.setOnClickListener {
            viewModel.addExpression("2")
        }
        binding.threeBtn.setOnClickListener {
            viewModel.addExpression("3")
        }
        binding.fourBtn.setOnClickListener {
            viewModel.addExpression("4")
        }
        binding.fiveBtn.setOnClickListener {
            viewModel.addExpression("5")
        }
        binding.sixBtn.setOnClickListener {
            viewModel.addExpression("6")
        }
        binding.sevenBtn.setOnClickListener {
            viewModel.addExpression("7")
        }
        binding.eightBtn.setOnClickListener {
            viewModel.addExpression("8")
        }
        binding.nineBtn.setOnClickListener {
            viewModel.addExpression("9")
        }
        binding.zeroBtn.setOnClickListener {
            viewModel.addExpression("0")
        }
        binding.pointBtn.setOnClickListener {
            viewModel.addExpression(".")
        }
        binding.deletBtn.setOnClickListener {
            viewModel.deleteExpression()
        }
        binding.clearBtn.setOnClickListener {
            viewModel.clear()
        }
        binding.plusBtn.setOnClickListener {
            viewModel.addExpression("+")
        }
        binding.minusBtn.setOnClickListener {
            viewModel.addExpression("-")
        }
        binding.multiplyBtn.setOnClickListener {
            viewModel.addExpression("*")
        }
        binding.divideBtn.setOnClickListener {
            viewModel.addExpression("/")
        }
        binding.leftParenthesis.setOnClickListener {
            viewModel.addExpression("(")
        }
        binding.rightParenthesis.setOnClickListener {
            viewModel.addExpression(")")
        }
        binding.equalBtn.setOnClickListener {
            viewModel.writeExpression()
        }
    }
    private fun showToastMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}