package com.ncc.kotlincraft.presentation.view.record

import android.annotation.SuppressLint
import android.graphics.Color
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
            val result = recordUseCase.getRecord()
            _records.postValue(result)
        }
    }

    fun deleteRecord(record: DomainRecord) {
        //백그라운드 스레드에서 데이터 삭제
        CoroutineScope(Dispatchers.IO).launch {
            _records.postValue(recordUseCase.deleteRecord(record))
        }
    }

    fun changeRecord(start: Int, end: Int) {
        records.value!![start]
        val startRecord = DomainRecord(records.value!![start].id, records.value!![end].expression)
        val endRecord = DomainRecord(records.value!![end].id, records.value!![start].expression)
        records.value!![start].expression = startRecord.expression
        records.value!![end].expression = endRecord.expression
        CoroutineScope(Dispatchers.IO).launch {
            recordUseCase.changeRecord(startRecord, endRecord)
        }
    }

    //usecase로 받아온 데이터를 filter로 색깔(string)에 따라 조건을 측정
    @SuppressLint("NotifyDataSetChanged")
    fun filteringRecord(color: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val records = recordUseCase.getRecord()
            when (color) {
                "yellow" -> {
                    val result = records.filter { it ->
                        it.expression!!.split("=").last().toDouble().toInt() in 0..10
                    }
                    _records.postValue(result)
                }
                "green" -> {
                    val result = records.filter { it ->
                        it.expression!!.split("=").last().toDouble().toInt() in 11..100
                    }
                    _records.postValue(result)
                }
                "red" -> {
                    val result = records.filter { it ->
                        it.expression!!.split("=").last().toDouble().toInt() in 101..1000
                    }
                    _records.postValue(result)
                }

                "blue" -> {
                    val result = records.filterNot { it ->
                        it.expression!!.split("=").last().toDouble()
                            .toInt() in 0..1000
                    }
                    _records.postValue(result)
                }
            }

        }
    }
}


