package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    //    private val viewModel: TaskModel by viewModel()
    private val viewModel: MainViewModel by viewModel()

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, 0, 0, 0)
//        viewBinding.recyclerView.setPadding(0, 0, 0, bottom)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewBinding.recyclerView.adapter = TaskRecyclerViewAdapter(viewModel.tasks)
        viewModel.getNotes()?.observe(viewLifecycleOwner, {
            viewBinding.recyclerView.adapter = NotesRecyclerViewAdapter(it)
        })


        viewBinding.btnSendNote.setOnClickListener {
            val titleText = viewBinding.etTypeNote.text.toString()
            if (titleText.isNotEmpty()) {
                viewModel.launch(Dispatchers.Main) {
                    viewModel.addNote(titleText)
                    viewBinding.recyclerView.adapter?.notifyDataSetChanged()
                }
//                viewModel.notes.add(Note(titleText))
//                viewBinding.recyclerView.adapter = NotesRecyclerViewAdapter(viewModel.notes)
                viewBinding.etTypeNote.text.clear()
            }
        }
    }
}