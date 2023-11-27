package com.ncc.kotlincraft.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ncc.kotlincraft.view.util.adapter.callback.DragDropCallback
import com.ncc.kotlincraft.view.listener.LongClickListener
import com.ncc.kotlincraft.R
import com.ncc.kotlincraft.data.db.entity.Record
import com.ncc.kotlincraft.view.util.adapter.RecordAdapter
import com.ncc.kotlincraft.data.db.RecordDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordActivity : AppCompatActivity(
) {
    var db: RecordDatabase? = null
    private lateinit var rv_record: RecyclerView

    // 받아올 records를 사용할 record List 생성
    var records = arrayListOf<Record>()
    private val listener = object : LongClickListener {
        override fun delete(record: Record) {
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
        setContentView(R.layout.activity_record)

        val btnMainToRecord = findViewById<AppCompatButton>(R.id.btn_recordToMain)
        rv_record = findViewById<RecyclerView>(R.id.rv_record)

        initRecyclerView()
        //RoomDb인스턴스 생성
        db = RecordDatabase.getInstance(this)

        //메인 스레드와는 별개로 데이터를 받아오는 비동기 작업
        CoroutineScope(Dispatchers.IO).launch {
            val recordDao = db!!.recordDao()
            records = recordDao.getAll() as ArrayList<Record>
            withContext(Dispatchers.Main) {
                recordAdapter.addItems(records)
                recordAdapter.notifyDataSetChanged()
            }
        }

        btnMainToRecord.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //Adapter에서 실행 할 수 있게 세팅
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteRecord(record: Record) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("삭제하시겠습니까?")
            .setPositiveButton(
                "삭제",
                DialogInterface.OnClickListener { dialog, which ->
                    //백그라운드 스레드에서 데이터 삭제
                    CoroutineScope(Dispatchers.IO).launch {
                        val recordDao = db!!.recordDao()
                        recordDao.deleteRecord(record)
                        records = recordDao.getAll() as ArrayList<Record>
                        withContext(Dispatchers.Main) {
//                            recordAdapter.addItems(records)
                            recordAdapter.notifyDataSetChanged()
                        }
                    }
                }
            )
            .setNegativeButton("취소", null)
        builder.show()
    }


    // start~end가 아니라 한칸 씩 작동하게 설정
    private fun changeRecord(start: Int, end: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val recordDao = db!!.recordDao()
            val startRecord = Record(records[start].id, records[end].expression)
            val endRecord = Record(records[end].id, records[start].expression)
            records[start] = startRecord
            records[end] = endRecord
            recordDao.updateRecord(startRecord)
            recordDao.updateRecord(endRecord)
            Log.d("작동 후", "${records}")
        }
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
    }

}