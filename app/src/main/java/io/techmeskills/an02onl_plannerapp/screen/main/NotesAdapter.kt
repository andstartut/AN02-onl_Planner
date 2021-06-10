package io.techmeskills.an02onl_plannerapp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.database.model.Note
import okhttp3.internal.notifyAll
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(
    private val onClick: (Note) -> Unit
) : ListAdapter<Note, NotesAdapter.NoteViewHolder>(NoteAdapterDiffCallback()), Filterable {

    var noteFilterList: MutableList<Note>?

    init {
        noteFilterList = currentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        )
    }

    private fun onItemClick(position: Int) = onClick(getItem(position))

    override fun getItemCount(): Int {
        return if (noteFilterList!!.isEmpty()) {
            currentList.size
        } else {
            noteFilterList!!.size
        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        if (noteFilterList!!.isEmpty()) {
            holder.bind(getItem(position))
        } else {
            holder.bind(noteFilterList!![position])
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                noteFilterList = if (charSearch.isEmpty()) {
                    currentList
                } else {
                    val resultList = ArrayList<Note>()
                    for (note in currentList) {
                        if (note.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(note)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = noteFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                noteFilterList = results?.values as MutableList<Note>
                notifyDataSetChanged()
            }
        }
    }

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    inner class NoteViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val ivCloud = itemView.findViewById<ImageView>(R.id.ivCloud)

        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(item: Note) {
            tvTitle.text = item.title
            if (item.setEvent) {
                tvDate.text = dateFormatter.format(item.date)
            }
            ivCloud.isVisible = item.cloudSync
        }
    }
}

class NoteAdapterDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.accountName == newItem.accountName
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.title == newItem.title &&
                oldItem.date == newItem.date &&
                oldItem.setEvent == newItem.setEvent &&
                oldItem.cloudSync == newItem.cloudSync
    }

}