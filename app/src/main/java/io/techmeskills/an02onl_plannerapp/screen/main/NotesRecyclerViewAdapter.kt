package io.techmeskills.an02onl_plannerapp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.techmeskills.an02onl_plannerapp.R

class NotesRecyclerViewAdapter(
    private val items: List<Note>,
    private val onClickListener: (Int) -> Unit
) : RecyclerView.Adapter<NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false),
            onClickListener
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class NoteViewHolder(itemView: View, private val onClickListener: (Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
    private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)

    fun bind(item: Note) {
        with(item) {
            tvTitle.text = item.title
            tvDate.text = item.date
            itemView.setOnClickListener { onClickListener(adapterPosition) }
        }
    }
}