package com.ncc.kotlincraft.data.db.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    // id는 유지 하고 내용을 바꾸는 방향으로 수정
    @ColumnInfo(name = "expression") var expression: String?,
    // write 과정에 연산한 색상을 저장 ( 호출 시 필터 과정이 생략되기 때문에 효율성이 더 향상 )
    @ColumnInfo(name = "color") var color: String?
)
