package com.ncc.kotlincraft

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecordDao {
    @Query("SELECT * FROM record ORDER BY record.id")
    fun getAll(): List<Record>

    @Insert
    fun insertRecord(record: Record)

    @Delete
    fun deleteRecord(record: Record)


    @Query("DELETE FROM record")
    fun deleteAllRecords()

    @Update
    fun updateRecord(records: List<Record>)
//    fun updateRecord(vararg records: Record)
}