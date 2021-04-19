package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.data.PersistentStorage
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList
import io.techmeskills.an02onl_plannerapp.screen.main.NotesAdapter as NotesAdapter

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override val viewBinding: FragmentMainBinding by viewBinding()

    private val viewModel: MainViewModel by viewModel()

    private val adapter = NotesAdapter(
        onClick = { note ->
            view?.findNavController()?.navigate(
                MainFragmentDirections.toNoteDetailsFragment(note)
            )
        }
    )


    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setPadding(0, 0, 0, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val persistentStorage = PersistentStorage(this.requireContext())

        viewBinding.recyclerView.adapter = adapter

        val adapterSpinner =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, viewModel.accountsList)
        viewBinding.spinner.adapter = adapterSpinner
        viewBinding.spinner.setSelection(adapterSpinner.getPosition(persistentStorage.getAccountName()))
        viewBinding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                persistentStorage.setAccountName(viewBinding.spinner.selectedItem.toString())
                viewModel.updateLiveData(viewBinding.spinner.selectedItem.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        viewModel.data?.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteWithUndo(adapter.currentList[viewHolder.adapterPosition]) { note ->
                    Snackbar.make(view, "Note removed", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", View.OnClickListener {
                            viewModel.addNote(note)
                        })
                        .show()
                }
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(viewBinding.recyclerView)

        viewBinding.btnAddNew.setOnClickListener {
            findNavController()
                .navigate(
                    MainFragmentDirections.toNoteDetailsFragment(null)
                )
        }

        viewBinding.btnAccountSetting.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toLoginFragment())
        }
    }
}

