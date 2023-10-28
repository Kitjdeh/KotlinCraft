package com.ncc.kotlincraft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val btnMainToRecord = findViewById<AppCompatButton>(R.id.btn_recordToMain)

        //RoomDb인스턴스 생성
        val db = Room.databaseBuilder(
            applicationContext,
            RecordDatabase::class.java, "record"
        ).build()
        CoroutineScope(Dispatchers.IO).launch {
            val recordDao = db.recordDao()
            val records: List<Record> = recordDao.getAll()
            Log.d("레코드", records.toString())
        }
        btnMainToRecord.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}