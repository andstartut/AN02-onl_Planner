package io.techmeskills.an02onl_plannerapp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.techmeskills.an02onl_plannerapp.R

class TaskRecyclerViewAdapter(private val taskList: List<Task>) :
    RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount() = taskList.size
}

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTask = itemView.findViewById<TextView>(R.id.tvTask)
    private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)

    fun bind(item: Task) {
        tvTask.text = item.task
        tvDate.text = item.date
    }
}
