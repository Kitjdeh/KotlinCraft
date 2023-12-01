package com.ncc.kotlincraft.listener

import com.ncc.kotlincraft.data.db.entity.Record

interface LongClickListener {
    fun delete(record: Record)

    //chage 주입을 여기 listner를 통해 주입
    fun change(start:Int,end:Int)
}