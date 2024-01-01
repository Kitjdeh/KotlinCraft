
package com.ncc.kotlincraft.presentation.listener

import com.ncc.kotlincraft.data.db.entity.Record
import com.ncc.kotlincraft.domain.model.DomainRecord


interface LongClickListener {
    fun delete(record: DomainRecord)

    //chage 주입을 여기 listner를 통해 주입
    fun change(start:Int,end:Int)
}