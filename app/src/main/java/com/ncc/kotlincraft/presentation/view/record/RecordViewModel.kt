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
    suspend fun deleteRecord(record: DomainRecord) {
        //백그라운드 스레드에서 데이터 삭제
        val delete = CoroutineScope(Dispatchers.IO).launch {
            recordUseCase.deleteRecord(record)
        }
        //join으로 삭제 로직 종료를 대기
        delete.join()
        //삭제 로직이 끝난후 다시 데이터를 불러와서 수정된 데이터로 리스트 뷰가 변동 되도록 설정
        CoroutineScope(Dispatchers.IO).launch {
            val records = recordUseCase.getRecord()
            _records.postValue(records)
        }
    }

    fun changeRecord(start: Int, end: Int) {
        records.value!![start]
        val startRecord = DomainRecord(records.value!![start].id, records.value!![end].expression)
        val endRecord = DomainRecord(records.value!![end].id, records.value!![start].expression)
        CoroutineScope(Dispatchers.IO).launch {
            recordUseCase.changeRecord(startRecord, endRecord)
        }
    }
}


