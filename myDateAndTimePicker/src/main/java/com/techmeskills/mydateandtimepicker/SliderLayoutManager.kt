package com.techmeskills.mydatepicker

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.sqrt


class SliderLayoutManager(context: Context?) : LinearLayoutManager(context) {

    init {
        orientation = VERTICAL
    }

    var callback: OnItemSelectedListener? = null
    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view!!

        // Smart snapping
        recyclerView.onFlingListener = null
        LinearSnapHelper().attachToRecyclerView(recyclerView)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        scaleDownView()
    }

    override fun scrollVerticallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        return if (orientation == VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dx, recycler, state)
            scaleDownView()
            scrolled
        } else {
            0
        }
    }

    private fun scaleDownView() {
        val mid = height / 2.0f
        for (i in 0 until childCount) {

            val child = getChildAt(i)
            val childMid = (getDecoratedTop(child!!) + getDecoratedBottom(child)) / 2.0f
            val distanceFromCenter = abs(mid - childMid)

            val scale = 1 - sqrt((distanceFromCenter / height).toDouble()).toFloat() * 0.66f

            child.scaleX = scale
            child.scaleY = scale
        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        // When scroll stops we notify on the selected item
        if (state == RecyclerView.SCROLL_STATE_IDLE) {

            // Find the closest child to the recyclerView center --> this is the selected item.
            val recyclerViewCenterX = getRecyclerViewCenterX()
            var minDistance = recyclerView.height
            var position = -1
            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                val childCenterX = getDecoratedTop(child) + (getDecoratedBottom(child) - getDecoratedTop(child)) / 2
                val newDistance = abs(childCenterX - recyclerViewCenterX)
                if (newDistance < minDistance) {
                    minDistance = newDistance
                    position = recyclerView.getChildLayoutPosition(child)
                }
            }

            // Notify on item selection
            callback?.onItemSelected(position)
        }
    }

    private fun getRecyclerViewCenterX(): Int {
        return (recyclerView.bottom - recyclerView.top) / 2 + recyclerView.top
    }

    interface OnItemSelectedListener {
        fun onItemSelected(layoutPosition: Int)
    }

    fun onItemSelected(): Int {
        return layoutDirection
    }
}