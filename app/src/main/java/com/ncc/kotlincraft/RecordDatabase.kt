package com.ncc.kotlincraft

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Record::class], version = 1)
abstract class RecordDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        private var DB: RoomDatabase? = null
        fun getInstance(context: Context): RoomDatabase {
            if (DB == null) {
                synchronized(
                    RoomDatabase::class
                ) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        RecordDatabase::class.java, "record"
                    ).build()
                }
            }
            return DB!!
        }
    }
}