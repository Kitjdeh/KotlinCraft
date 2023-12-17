package com.ncc.kotlincraft.domain.usecase


import android.util.Log
import com.ncc.kotlincraft.data.entity.Record
import com.ncc.kotlincraft.data.repository.RecordRepositoryImpl
import com.ncc.kotlincraft.domain.model.DomainRecord
import com.ncc.kotlincraft.domain.repository.RecordRepository

//뷰 모델에서 바로 레퍼지토리를 연결하지 않고 usecase로 연결을 하여 수정이 일어나도 해당 부분만 수정하도록 설정
class RecordUseCase

{
    private val repository = RecordRepositoryImpl()
    fun getRecord(): List<DomainRecord> {
        return repository.getRecords()
    }

    fun getRecord(repository: RecordRepository): List<DomainRecord> {
        return repository.getRecords()
    }

    fun writeRecord(domainRecord: DomainRecord) = repository.writeRecord(domainRecord)

    fun deleteRecord(record: DomainRecord) = repository.deleteRecord(record)

    fun changeRecord(start: DomainRecord, end: DomainRecord) = repository.changeRecord(start, end)

}