package com.ncc.kotlincraft

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class Record(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo(name = "expression") val expression : String?,

)
