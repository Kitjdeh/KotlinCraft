package com.ncc.kotlincraft

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordActivity : AppCompatActivity(


) {

    var db: RecordDatabase? = null

    // 받아올 records를 사용할 record List 생성
    var records = arrayListOf<Record>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val btnMainToRecord = findViewById<AppCompatButton>(R.id.btn_recordToMain)
        val rv_record = findViewById<RecyclerView>(R.id.rv_record)
        //RoomDb인스턴스 생성
        db = RecordDatabase.getInstance(this)


        //메인 스레드와는 별개로 데이터를 받아오는 비동기 작업
        CoroutineScope(Dispatchers.IO).launch {
            val recordDao = db!!.recordDao()
            records = recordDao.getAll() as ArrayList<Record>
            //백그라운드 스레드 작업이 끝났으니 main스레드 에서 UI 변경 작업 진행
            withContext(Dispatchers.Main) {
                // 어댑터 연결
                rv_record.adapter = RecordAdapter(records) { record -> deleteRecord(record) }
            }
//            //records가 채워 졌으니 adapter와  연결
//            var recordAdapter = RecordAdapter(records)
//            // 어댑터 연결
//            rv_record.adapter = recordAdapter
//            // 어댑터의 layoutmanager 연결
//            rv_record.layoutManager = LinearLayoutManager(this@RecordActivity)
        }

        initRecyclerView()
        btnMainToRecord.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //Adapter에서 실행 할 수 있게 세팅
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
                    }

                }
            )
            .setNegativeButton("취소", null)
        builder.show()
    }

    private fun initRecyclerView() {
        val rv_record = findViewById<RecyclerView>(R.id.rv_record)
        //records가 채워 졌으니 adapter와  연결
        var recordAdapter = RecordAdapter(records) { record -> deleteRecord(record) }
//        var recordAdapter = RecordAdapter(records)
        // 어댑터 연결
        rv_record.adapter = recordAdapter
        // 어댑터의 layoutmanager 연결
        rv_record.layoutManager = LinearLayoutManager(this@RecordActivity)

    }

}