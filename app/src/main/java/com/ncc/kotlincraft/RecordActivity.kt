package com.ncc.kotlincraft

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordActivity : AppCompatActivity() {

    var db: RecordDatabase? = null

    // 받아올 records를 사용할 record List 생성
    var records = arrayListOf<Record>(

    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val btnMainToRecord = findViewById<AppCompatButton>(R.id.btn_recordToMain)
        val rv_record = findViewById<RecyclerView>(R.id.rv_record)


        //RoomDb인스턴스 생성
        db = RecordDatabase.getInstance(this)


//        println("TEST TEST TEST DB2`:${db}")
        CoroutineScope(Dispatchers.IO).launch {
            val recordDao = db!!.recordDao()
            records = recordDao.getAll() as ArrayList<Record>
            //records가 채워 졌으니 adapter와  연결
            var recordAdapter = RecordAdapter(records)
            // 어댑터 연결
            rv_record.adapter = recordAdapter
            // 어댑터의 layoutmanager 연결
            rv_record.layoutManager = LinearLayoutManager(this@RecordActivity)

        }
        btnMainToRecord.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}