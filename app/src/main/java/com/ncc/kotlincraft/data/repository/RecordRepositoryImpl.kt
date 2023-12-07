package com.ncc.kotlincraft.data.repository

import android.util.Log
import com.ncc.kotlincraft.data.db.RecordDatabase
import com.ncc.kotlincraft.data.entity.Record
import com.ncc.kotlincraft.data.mapper.mapperDataToDomain
import com.ncc.kotlincraft.data.mapper.mapperDomainToData
import com.ncc.kotlincraft.data.repository.local.RecordDataSource
import com.ncc.kotlincraft.data.repository.local.RecordDataSourceImpl
import com.ncc.kotlincraft.domain.model.DomainRecord
import com.ncc.kotlincraft.domain.repository.RecordRepository

class RecordRepositoryImpl

    : RecordRepository {

    private val recordDataSource = RecordDataSourceImpl()
    override fun getRecords(): List<DomainRecord> {
        return mapperDataToDomain(recordDataSource.getRecord())
    }

    override fun changeRecord(start: DomainRecord, end: DomainRecord) {
        recordDataSource.changeRecord(mapperDomainToData(start), mapperDomainToData(end))
    }

    override fun writeRecord(record: DomainRecord) {
        recordDataSource.writeRecord(mapperDomainToData(record))
    }

    override fun deleteRecord(record: DomainRecord): List<DomainRecord> {
        return mapperDataToDomain(recordDataSource.deleteRecord(mapperDomainToData(record)))
    }
}