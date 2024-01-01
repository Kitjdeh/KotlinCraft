package com.ncc.kotlincraft.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class DomainRecord(
    val id: Int?,
    var expression: String?,
    var color: String?
) {
    fun filterColor(color: String) {
//        when (color) {
//            "yellow" -> {
//                result = records.filter { it ->
//                    it.expression!!.split("=").last().toDouble().toInt() in 0..10
//                }
//            }
//
//            "green" -> {
//                result = records.filter { it ->
//                    it.expression!!.split("=").last().toDouble().toInt() in 11..100
//                }
//            }
//
//            "red" -> {
//                result = records.filter { it ->
//                    it.expression!!.split("=").last().toDouble().toInt() in 101..1000
//                }
//            }
//
//            "blue" -> {
//                result = records.filterNot { it ->
//                    it.expression!!.split("=").last().toDouble()
//                        .toInt() in 0..1000
//                }
//            }
//        }
    }
}
