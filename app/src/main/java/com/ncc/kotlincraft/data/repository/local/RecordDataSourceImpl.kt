package com.ncc.kotlincraft.data.repository.local

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.ncc.kotlincraft.app.App
import com.ncc.kotlincraft.data.db.RecordDatabase
import com.ncc.kotlincraft.data.db.dao.RecordDao
import com.ncc.kotlincraft.data.entity.Record
import kotlin.coroutines.coroutineContext

class RecordDataSourceImpl

    : RecordDataSource {
    private val context = App.getContext()
    private val recordDao = RecordDatabase.getInstance(context)!!.recordDao()
    override fun getRecord(): List<Record> {
        return recordDao.getAll()
    }

    override fun writeRecord(record: Record) {
        recordDao.insertRecord(record)
    }

    //n개의 리스트가 n-1개로 변경된 데이터 값을 받아 오게 한다.
    override fun deleteRecord(record: Record): List<Record> {
        recordDao.deleteRecord(record)
        return recordDao.getAll()
    }

    override fun changeRecord(start: Record, end: Record) {
        recordDao.updateRecord(start)
        recordDao.updateRecord(end)
    }
}