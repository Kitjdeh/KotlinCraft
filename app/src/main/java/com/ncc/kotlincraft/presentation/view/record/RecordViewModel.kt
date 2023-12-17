package com.ncc.kotlincraft.presentation.view.record

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ncc.kotlincraft.data.db.RecordDatabase
import com.ncc.kotlincraft.domain.model.DomainRecord
import com.ncc.kotlincraft.domain.usecase.RecordUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordViewModel : ViewModel() {
    var db: RecordDatabase? = null

    //recordUsecase 인스턴스 생성
    private val recordUseCase = RecordUseCase()

    //RecordList 데이터
    private val _records = MutableLiveData<List<DomainRecord>>()
    val records: LiveData<List<DomainRecord>>
        get() = _records

    fun getRecord() {
        CoroutineScope(Dispatchers.IO).launch {
            val records = recordUseCase.getRecord()
            _records.postValue(records)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteRecord(record: DomainRecord) {
        //백그라운드 스레드에서 데이터 삭제
        CoroutineScope(Dispatchers.IO).launch {
            recordUseCase.deleteRecord(record)
            val records = recordUseCase.getRecord()
            _records.postValue(records)
        }
    }

    fun changeRecord(start: Int, end: Int) {
        records.value!![start]
        val startRecord = DomainRecord(records.value!![start].id, records.value!![end].expression)
        val endRecord = DomainRecord(records.value!![end].id, records.value!![start].expression)
        recordUseCase.changeRecord(startRecord, endRecord)
//        CoroutineScope(Dispatchers.IO).launch {
//            val recordDao = db!!.recordDao()
//            val startRecord = DomainRecord(records[start].id, records[end].expression)
//            val endRecord = DomainRecord(records[end].id, records[start].expression)
//            records[start] = startRecord
//            records[end] = endRecord
//            recordDao.updateRecord(startRecord)
//            recordDao.updateRecord(endRecord)
//            Log.d("작동 후", "${records}")
//        }
    }
}