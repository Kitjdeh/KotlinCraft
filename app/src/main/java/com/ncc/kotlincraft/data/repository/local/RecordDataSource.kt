package com.ncc.kotlincraft.data.repository.local

import com.ncc.kotlincraft.data.db.entity.Record

interface RecordDataSource {
    fun getRecord():List<Record>

    fun writeRecord(record: Record)

    fun deleteRecord(record: Record):List<Record>

    fun changeRecord(start:Record,end:Record)
}