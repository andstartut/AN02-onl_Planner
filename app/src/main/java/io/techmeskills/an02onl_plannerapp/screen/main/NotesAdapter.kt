package io.techmeskills.an02onl_plannerapp.screen.main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.database.model.Note
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(
    private val onClick: (Note) -> Unit,
    private val onLongClick: (Note) -> Unit,
    private val onLongClickShare: (Note) -> Unit,
    private val onLongClickDelete: (Note) -> Unit
) : ListAdapter<Note, NotesAdapter.NoteViewHolder>(NoteAdapterDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        )
    }

    private fun onItemClick(position: Int) = onClick(getItem(position))

    private fun onLongItemClick(position: Int) = onLongClick(getItem(position))

    private fun onLongItemClickShare(position: Int) = onLongClickShare(getItem(position))

    private fun onLongItemClickDelete(position: Int) = onLongClickDelete(getItem(position))

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    inner class NoteViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val cardView = itemView.findViewById<MaterialCardView>(R.id.cardView)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val ivCloud = itemView.findViewById<ImageView>(R.id.ivCloud)
        private val ivPin = itemView.findViewById<ImageView>(R.id.ivPin)

        private val pop = PopupMenu(itemView.context, itemView)

        init {
            pop.inflate(R.menu.note_context)
            val pinItem = pop.menu.findItem(R.id.note_context_pin)

            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }

            itemView.setOnLongClickListener {
                pop.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.note_context_pin -> {
                            onLongItemClick(adapterPosition)
                            if (ivPin.isVisible) {
                                pinItem.title = NOTE_CONTEXT_MENU_UNPIN
                            } else {
                                pinItem.title = NOTE_CONTEXT_MENU_PIN
                            }
                        }
                        R.id.note_context_share -> {
                            onLongItemClickShare(adapterPosition)
                        }
                        R.id.note_context_delete -> {
                            onLongItemClickDelete(adapterPosition)
                        }
                    }
                    true
                }
                pop.show()
                true
            }
        }

        fun bind(item: Note) {
            tvTitle.text = item.title
            if (item.setEvent) {
                tvDate.text = dateFormatter.format(item.date)
            }
            ivCloud.isVisible = item.cloudSync
            cardView.setCardBackgroundColor(Color.parseColor(item.color))
            ivPin.isVisible = item.pinned
        }
    }

    companion object {
        const val NOTE_CONTEXT_MENU_PIN = "Pin"
        const val NOTE_CONTEXT_MENU_UNPIN = "Unpin"
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
                oldItem.cloudSync == newItem.cloudSync &&
                oldItem.color == newItem.color &&
                oldItem.pinned == newItem.pinned
    }
}
