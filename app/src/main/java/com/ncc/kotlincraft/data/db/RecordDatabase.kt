package com.ncc.kotlincraft.data.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ncc.kotlincraft.data.entity.Record
import com.ncc.kotlincraft.data.db.dao.RecordDao
import com.ncc.kotlincraft.domain.usecase.RecordUseCase


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
                    DB = Room.databaseBuilder(
                        context.applicationContext,
                        RecordDatabase::class.java, "record"
                    ).build()
                }
            }
            return DB
        }
//        fun getDomainInstance(applicationContext: Context): RecordDatabase? {
//            if (DB == null) {
//                synchronized(
//                    RecordDatabase::class
//                ) {
//                    //return 이 DB인데 DB값을 안바꿔서 수정
//                    DB = Room.databaseBuilder(
//                        applicationContext.applicationContext,
//                        RecordDatabase::class.java, "record"
//                    ).build()
//                }
//            }
//            return DB
//        }
//        fun getInstance(context: Context): RecordDatabase? {
//            if (DB == null) {
//                synchronized(
//                    RecordDatabase::class
//                ) {
//                    //return 이 DB인데 DB값을 안바꿔서 수정
//                    DB = Room.databaseBuilder(
//                        context.applicationContext,
//                        RecordDatabase::class.java, "record"
//                    ).build()
//                }
//            }
//            return DB
//        }
    }
}