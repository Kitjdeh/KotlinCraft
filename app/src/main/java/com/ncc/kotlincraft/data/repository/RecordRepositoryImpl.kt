package com.ncc.kotlincraft.data.repository


import com.ncc.kotlincraft.data.mapper.mapperToDomainRecord
import com.ncc.kotlincraft.data.mapper.mapperToRecord
import com.ncc.kotlincraft.data.repository.local.RecordDataSourceImpl
import com.ncc.kotlincraft.domain.model.DomainRecord
import com.ncc.kotlincraft.domain.repository.RecordRepository

class RecordRepositoryImpl

    : RecordRepository {

    private val recordDataSource = RecordDataSourceImpl()
    override fun getRecords(): List<DomainRecord> {
        return recordDataSource.getRecord().mapperToDomainRecord()
    }

    override fun changeRecord(start: DomainRecord, end: DomainRecord) {
        recordDataSource.changeRecord(start.mapperToRecord(), end.mapperToRecord())
    }

    override fun writeRecord(record: DomainRecord) {
        recordDataSource.writeRecord(record.mapperToRecord())
    }

    override fun deleteRecord(record: DomainRecord): List<DomainRecord> {
        return recordDataSource.deleteRecord(record.mapperToRecord()).mapperToDomainRecord()
    }
}