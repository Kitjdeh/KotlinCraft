package com.ncc.kotlincraft.presentation.view.record.adapter


import android.graphics.Color
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncc.kotlincraft.presentation.listener.DragDropListener
import com.ncc.kotlincraft.presentation.listener.LongClickListener
import com.ncc.kotlincraft.R
import com.ncc.kotlincraft.databinding.RecordRvItemBinding
import com.ncc.kotlincraft.domain.model.DomainRecord


class RecordAdapter(
) :
//interface DragDropListener를 Adapter에 넣은 후 override로 Adapter의 작용을 추가한다.
    RecyclerView.Adapter<RecordAdapter.ViewHolder>(), DragDropListener {


    inner class ViewHolder(binding: RecordRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val expression = binding.rvItem
        val dragBtn = binding.rvItemDrag
    }

    private val items = mutableListOf<DomainRecord>()
    private lateinit var listener: LongClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.record_rv_item, parent, false)
        return ViewHolder(RecordRvItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.expression.text = items[position].expression


        // "Adapter에서 결과값의 크기 연산 하는 로직을 color 기반으로 변경"
        holder.expression.setTextColor(Color.BLACK)
        when (val color = items[position].color) {
            "yellow" -> {
                holder.expression.setBackgroundColor(Color.YELLOW)
            }
            "green" -> {
                holder.expression.setBackgroundColor(Color.GREEN)
            }
            "red" -> {
                holder.expression.setBackgroundColor(Color.RED)
            }
            else -> {
                holder.expression.setTextColor(Color.WHITE)
                holder.expression.setBackgroundColor(Color.BLUE)
            }
        }
        //받아온 clickListner함수에 해당 itemList 주입
        holder.expression.setOnLongClickListener {
            listener.delete(items[position])
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun addItems(records: ArrayList<DomainRecord>) {
        this.items.addAll(records)
    }

    fun changeItems(records: ArrayList<DomainRecord>) {
        this.items.clear()
        this.items.addAll(records)
    }

    fun addListener(listener: LongClickListener) {
        this.listener = listener
    }

    //ItemTouchHelper가 움직인 결과를 adapter에 적용한다.
    override fun moveItem(start: Int, end: Int) {
        notifyItemMoved(start, end)
        listener.change(start, end)
    }

}