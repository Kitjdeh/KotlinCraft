package com.ncc.kotlincraft.presentation.view.record

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ncc.kotlincraft.presentation.listener.LongClickListener
import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ncc.kotlincraft.presentation.view.record.adapter.callback.DragDropCallback
import com.ncc.kotlincraft.R
import com.ncc.kotlincraft.presentation.view.record.adapter.RecordAdapter
import com.ncc.kotlincraft.data.db.RecordDatabase
import com.ncc.kotlincraft.databinding.ActivityMainBinding
import com.ncc.kotlincraft.databinding.ActivityRecordBinding
import com.ncc.kotlincraft.domain.model.DomainRecord
import com.ncc.kotlincraft.domain.usecase.RecordUseCase
import com.ncc.kotlincraft.presentation.view.main.MainActivity
import com.ncc.kotlincraft.presentation.view.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val view = binding.root
        setContentView(view)
        viewModel.records.observe(this) { result ->
            records = result as ArrayList<DomainRecord>
            recordAdapter.addItems(records)
            recordAdapter.notifyDataSetChanged()
        }
        rv_record = binding.rvRecord
        initRecyclerView()
        binding.btnRecordToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    //Adapter에서 실행 할 수 있게 세팅
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteRecord(record: DomainRecord) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("삭제하시겠습니까?")
            .setPositiveButton(
                "삭제",
                DialogInterface.OnClickListener { dialog, which ->
                    viewModel.deleteRecord(record)
                }
            )
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
        viewModel.getRecord()
    }

}