
package com.ncc.kotlincraft.presentation.view.record.adapter.callback


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ncc.kotlincraft.presentation.listener.DragDropListener

class DragDropCallback(private val listener: DragDropListener):ItemTouchHelper.Callback() {

    //drag 할 경우 방향(위,아래)를 결정
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val flag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(flag,0)
    }

    //start에서 end로 이동 할 경우 해당 데이터를 전달
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        listener.moveItem(viewHolder.adapterPosition,target.adapterPosition)
//        Log.d("DragDropCallBack클래스","${viewHolder.adapterPosition} ${target.adapterPosition}")
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }
}