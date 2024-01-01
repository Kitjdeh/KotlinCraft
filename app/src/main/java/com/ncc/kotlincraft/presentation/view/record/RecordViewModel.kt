package com.ncc.kotlincraft.presentation.view.record

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    //필터링 조건 없이 전체를 불러오는 함수 - init과 동시에 한번만 작동
    fun getAllRecord() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val result = recordUseCase.getAllRecord()
            _records.postValue(result)
        }
    }

    //클릭한 색깔에 맞춰서 usecase요청하고 해당 값을 그대로 전달
    fun getRecord(color: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val result = recordUseCase.getRecord(color)
            _records.postValue(result)
        }
    }

    //백그라운드 스레드에서 데이터 삭제
    fun deleteRecord(record: DomainRecord) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _records.postValue(recordUseCase.deleteRecord(record))
        };
    }

    fun changeRecord(start: Int, end: Int) {
        val startRecord =
            records.value!![end].copy(id = start)
//            DomainRecord(records.value!![start].id, records.value!![end].expression)
        val endRecord =
            records.value!![start].copy(id = end)
//            DomainRecord(records.value!![end].id, records.value!![start].expression)
        records.value!![start].expression = startRecord.expression
        records.value!![end].expression = endRecord.expression
        CoroutineScope(Dispatchers.IO).launch {
            recordUseCase.changeRecord(startRecord, endRecord)
        }
    }


}


