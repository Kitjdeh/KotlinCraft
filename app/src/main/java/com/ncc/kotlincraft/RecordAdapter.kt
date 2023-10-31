package com.ncc.kotlincraft

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecordAdapter(val itemList: List<Record>): RecyclerView.Adapter<RecordAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val expression = itemView.findViewById<TextView>(R.id.rv_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_rv_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordAdapter.ViewHolder, position: Int) {
        holder.expression.text = itemList[position].expression
        val result = itemList[position].expression!!.split("=").last()
        if (result.toDouble().toInt() > 0){
            when (val number = result.toDouble().toInt() ) {
                in 0..10 -> {
                    holder.expression.setBackgroundColor(Color.YELLOW)
                }
                in 11..100 -> {

                    holder.expression.setBackgroundColor(Color.GREEN)
                }
                in 101..1000 -> {

                    holder.expression.setBackgroundColor(Color.RED)
                }
                else -> {

                    holder.expression.setBackgroundColor(Color.BLUE)
                }
            }

        }

    }

    override fun getItemCount(): Int {
       return itemList.count()
    }

}