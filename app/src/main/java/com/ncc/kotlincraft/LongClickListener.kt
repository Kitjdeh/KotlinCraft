package com.ncc.kotlincraft

interface LongClickListener {
    fun delete(record:Record)

    //chage 주입을 여기 listner를 통해 주입
    fun change(start:Int,end:Int)
}