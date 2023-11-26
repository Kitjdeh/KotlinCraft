package com.ncc.kotlincraft.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ncc.kotlincraft.listener.DragDropListener
import com.ncc.kotlincraft.listener.LongClickListener
import com.ncc.kotlincraft.R
import com.ncc.kotlincraft.db.entity.Record

class RecordAdapter(
) :
//interface DragDropListener를 Adapter에 넣은 후 override로 Adapter의 작용을 추가한다.
    RecyclerView.Adapter<RecordAdapter.ViewHolder>(), DragDropListener {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expression = itemView.findViewById<TextView>(R.id.rv_item)
    }

    private val Items = mutableListOf<Record>()

    private lateinit var listener: LongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.record_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.expression.text = Items[position].expression
        // = 기준으로 결과 값 변경
        val result = Items[position].expression!!.split("=").last()
//        if (result.isDigitsOnly()) {
        when (val number = result.toDouble().toInt()) {
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
//        }

        //받아온 clickListner함수에 해당 itemList 주입
        holder.itemView.setOnLongClickListener {
            listener.delete(Items[position])
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {

        return Items.count()
    }

    //
    fun addItems(records: List<Record>) {
        this.Items.addAll(records)
    }

    //    fun dragDropListener(listener: ItemTouchHelper.)
    fun addListener(listener: LongClickListener) {
        this.listener = listener
    }

    //ItemTouchHelper가 움직인 결과를 adapter에 적용한다.
    override fun moveItem(start: Int, end: Int) {
//        Log.d("어댑터moveImte","${start},${end}")
        notifyItemMoved(start, end)
        listener.change(start, end)

    }

}