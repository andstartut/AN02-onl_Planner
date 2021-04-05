package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.screen.addNew.AddNewFragment
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.data.observe(viewLifecycleOwner, {
            viewBinding.recyclerView.adapter = NotesRecyclerViewAdapter(it)
        })

        viewBinding.btnAddNew.setOnClickListener { view ->
            view.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToAddNewFragment2())
        }
        setFragmentResultListener(AddNewFragment.NEW_NOTE) { key, bundle ->
            val note = bundle.getString(AddNewFragment.NOTE_TEXT)
            val date = bundle.getString(AddNewFragment.DATE)
            if (date.isNullOrBlank()) {
                viewModel.addNote(note!!)
            } else {
                viewModel.addNote(note!!, date!!)
            }
//            viewBinding.recyclerView.scrollToPosition(viewBinding.recyclerView.adapter!!.itemCount - 1)
        }
//                viewBinding.recyclerView.adapter?.notifyDataSetChanged()
    }
}