package com.ncc.kotlincraft.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class DomainRecord(
    val id: Int?,
    var expression : String?,
)