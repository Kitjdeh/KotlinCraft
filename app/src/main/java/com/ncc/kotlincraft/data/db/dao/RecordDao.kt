package com.ncc.kotlincraft.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ncc.kotlincraft.data.entity.Record

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
    fun updateRecord(record: Record)
//    fun updateRecord(vararg records: Record)
}