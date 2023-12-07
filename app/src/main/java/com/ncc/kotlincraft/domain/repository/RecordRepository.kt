package com.ncc.kotlincraft.domain.repository

import com.ncc.kotlincraft.data.entity.Record
import com.ncc.kotlincraft.domain.model.DomainRecord


//Repository
interface RecordRepository {
    fun getRecords(): List<DomainRecord>

    fun changeRecord(start: DomainRecord, end: DomainRecord)

    fun writeRecord(record: DomainRecord)

    fun deleteRecord(record: DomainRecord): List<DomainRecord>
}