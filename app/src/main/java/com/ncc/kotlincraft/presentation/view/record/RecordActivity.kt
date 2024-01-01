package com.ncc.kotlincraft.presentation.view.record

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ncc.kotlincraft.presentation.listener.LongClickListener
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ncc.kotlincraft.presentation.view.record.adapter.callback.DragDropCallback
import com.ncc.kotlincraft.presentation.view.record.adapter.RecordAdapter
import com.ncc.kotlincraft.databinding.ActivityRecordBinding
import com.ncc.kotlincraft.domain.model.DomainRecord
import com.ncc.kotlincraft.presentation.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordActivity : AppCompatActivity(
) {
    private lateinit var rv_record: RecyclerView
    private lateinit var binding: ActivityRecordBinding

    private val viewModel: RecordViewModel by viewModels()

    // 받아올 records를 사용할 record List 생성
    var records = arrayListOf<DomainRecord>()

    private val listener = object : LongClickListener {
        override fun delete(record: DomainRecord) {
            deleteRecord(record)
        }

        override fun change(start: Int, end: Int) {
            changeRecord(start, end)
        }
    }
    private val recordAdapter = RecordAdapter()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rv_record = binding.rvRecord
        initRecyclerView()
        initLiveData()
        binding.btnRecordToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        // view에선 클릭 시 해당 색상 string 값을 전달 만 함
        binding.yellow.setOnClickListener {
            viewModel.getRecord("yellow")
        }
        binding.green.setOnClickListener {
            viewModel.getRecord("green")
        }
        binding.red.setOnClickListener {
            viewModel.getRecord("red")
        }
        binding.blue.setOnClickListener {
            viewModel.getRecord("blue")
        }
    }
    //AlertDialog 세팅, 삭제 시 뷰모델 함수 실행 하도록 설정
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteRecord(record: DomainRecord) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("삭제하시겠습니까?")
            .setPositiveButton(
                "삭제"
            ) { _, _ ->
                viewModel.deleteRecord(record)
            }
            .setNegativeButton("취소", null)
        builder.show()
    }

    // start~end가 아니라 한칸 씩 작동하게 설정
    private fun changeRecord(start: Int, end: Int) {
        viewModel.changeRecord(start, end)
    }

    private fun initRecyclerView() {
        rv_record.apply {
            layoutManager = LinearLayoutManager(this@RecordActivity)
            adapter = recordAdapter
        }
        recordAdapter.addListener(listener)
        val dragDropCallback = DragDropCallback(recordAdapter)
        val itemTouchHelper = ItemTouchHelper(dragDropCallback)
        itemTouchHelper.attachToRecyclerView(rv_record)
        viewModel.getAllRecord()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun initLiveData(){
        viewModel.records.observe(this) { result ->
            records = result as ArrayList<DomainRecord>
            recordAdapter.changeItems(records)
            recordAdapter.notifyDataSetChanged()
        }
    }

}