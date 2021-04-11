package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.screen.noteDetails.NoteDetailsFragment
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import io.techmeskills.an02onl_plannerapp.screen.main.NotesRecyclerViewAdapter as NotesRecyclerViewAdapter

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    //    private val viewModel: TaskModel by viewModel()
    private val viewModel: MainViewModel by viewModel()

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, 0, 0, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.data.observe(viewLifecycleOwner, { it ->
            viewBinding.recyclerView.adapter = NotesRecyclerViewAdapter(
                it,
                onClickListener = {
                    setFragmentResultListener(NoteDetailsFragment.EDIT_NOTE) { key, bundle ->
                        val note = bundle.getString(NoteDetailsFragment.NOTE_TEXT)
                        val date = bundle.getString(NoteDetailsFragment.DATE)
                        val position = it
                        date?.let {
                            viewModel.editNote(note!!, date, position)
                        }
                    }
                    val bundle = bundleOf(
                        Pair(NoteDetailsFragment.TOOLBAR, TOOLBAR_EDIT),
                        Pair(NoteDetailsFragment.NOTE_TEXT, viewModel.data.value?.get(it)?.title),
                        Pair(NoteDetailsFragment.DATE, viewModel.data.value?.get(it)?.date),
                    )
                    view.findNavController().navigate(R.id.action_mainFragment_to_noteDetailsFragment,
                    bundle)
                })
//            viewBinding.recyclerView.scrollToPosition(viewBinding.recyclerView.adapter!!.itemCount - 1)
        })

        viewBinding.btnAddNew.setOnClickListener {
            view.findNavController()
                .navigate(
                    MainFragmentDirections.actionMainFragmentToNoteDetailsFragment(
                        TOOLBAR_ADD,
                        ""
                    )
                )
        }

        setFragmentResultListener(NoteDetailsFragment.NEW_NOTE) { key, bundle ->
            val note = bundle.getString(NoteDetailsFragment.NOTE_TEXT)
            val date = bundle.getString(NoteDetailsFragment.DATE)
            date?.let{
                viewModel.addNote(note!!, date)
            }
//            viewBinding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }
    companion object {
        const val TOOLBAR_EDIT = "Edit Note"
        const val TOOLBAR_ADD = "Add new note"
    }
}
