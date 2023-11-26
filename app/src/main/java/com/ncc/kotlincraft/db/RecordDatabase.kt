package com.ncc.kotlincraft.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ncc.kotlincraft.db.entity.Record
import com.ncc.kotlincraft.db.dao.RecordDao


@Database(entities = [Record::class], version = 1)
abstract class RecordDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        private var DB: RecordDatabase? = null
        fun getInstance(context: Context): RecordDatabase? {
            if (DB == null) {
                synchronized(
                    RecordDatabase::class
                ) {
                    //return 이 DB인데 DB값을 안바꿔서 수정
                    DB =Room.databaseBuilder(
                        context.applicationContext,
                        RecordDatabase::class.java, "record"
                    ).build()
                }
            }
            return DB
        }
    }
}