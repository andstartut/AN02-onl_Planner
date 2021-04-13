package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.screen.noteDetails.NoteDetailsFragment
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import io.techmeskills.an02onl_plannerapp.screen.main.NotesAdapter as NotesAdapter

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()

    private val adapter = NotesAdapter(
        onClick = { note ->
            view?.findNavController()?.navigate(
                MainFragmentDirections.actionMainFragmentToNoteDetailsFragment(note)
            )
        },
        onDelete = {int -> 
            viewModel.deleteNote(int)
        }
    )

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, 0, 0, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.recyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewBinding.btnAddNew.setOnClickListener {
            findNavController()
                .navigate(
                    MainFragmentDirections.actionMainFragmentToNoteDetailsFragment(null)
                )
        }

        setFragmentResultListener(NoteDetailsFragment.NOTE_RESULT) { key, bundle ->
            bundle.getParcelable<Note>(NoteDetailsFragment.NOTE)?.let {
                if (it.id <= NoteDetailsFragment.NEW_NOTE_INDEX) {
                    viewModel.addNote(it)
                } else {
                    viewModel.editNote(it)
                }
            }
//            viewBinding.recyclerView.adapter?.notifyDataSetChanged()
        }

        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(viewBinding.recyclerView)
    }
}

