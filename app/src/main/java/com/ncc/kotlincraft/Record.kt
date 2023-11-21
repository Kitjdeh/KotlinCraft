package com.ncc.kotlincraft

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    // id는 유지 하고 내용을 바꾸는 방향으로 수정
    @ColumnInfo(name = "expression") var expression : String?,

)
