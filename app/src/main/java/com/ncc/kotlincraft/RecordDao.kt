package com.ncc.kotlincraft

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Query("SELECT * FROM record")
    fun getAll(): List<Record>


    @Insert
    fun insertRecord(record: Record)
}