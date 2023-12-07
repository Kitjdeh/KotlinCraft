package com.ncc.kotlincraft.presentation.view.record

import android.annotation.SuppressLint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ncc.kotlincraft.presentation.view.record.adapter.callback.DragDropCallback
import com.ncc.kotlincraft.R
import com.ncc.kotlincraft.presentation.view.record.adapter.RecordAdapter
import com.ncc.kotlincraft.data.db.RecordDatabase
import com.ncc.kotlincraft.data.repository.RecordRepositoryImpl
import com.ncc.kotlincraft.data.repository.local.RecordDataSourceImpl
import com.ncc.kotlincraft.domain.model.DomainRecord
import com.ncc.kotlincraft.domain.usecase.RecordUseCase
import com.ncc.kotlincraft.presentation.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordActivity : AppCompatActivity(
) {
    var db: RecordDatabase? = null
    private lateinit var rv_record: RecyclerView


    // 받아올 records를 사용할 record List 생성
    var records = arrayListOf<DomainRecord>()

    //recordUsecase 인스턴스 생성
    private val recordUseCase = RecordUseCase()

    private val recordAdapter = RecordAdapter()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        CoroutineScope(Dispatchers.IO).launch {
            records = recordUseCase.getRecord() as ArrayList<DomainRecord>
            withContext(Dispatchers.Main) {
                recordAdapter.addItems(records)
                recordAdapter.notifyDataSetChanged()
            }
        }


        val btnMainToRecord = findViewById<AppCompatButton>(R.id.btn_recordToMain)
        rv_record = findViewById<RecyclerView>(R.id.rv_record)

        initRecyclerView()

        recordAdapter.addItems(records)
        recordAdapter.notifyDataSetChanged()

        btnMainToRecord.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun initRecyclerView() {

        rv_record.apply {
            layoutManager = LinearLayoutManager(this@RecordActivity)
            adapter = recordAdapter
        }
//        recordAdapter.addListener(listener)
        val dragDropCallback = DragDropCallback(recordAdapter)
        val itemTouchHelper = ItemTouchHelper(dragDropCallback)
        itemTouchHelper.attachToRecyclerView(rv_record)
    }

}