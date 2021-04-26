package io.techmeskills.an02onl_plannerapp.screen.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        viewBinding.recyclerView.adapter = adapter

        viewModel.spinnerDataLD.observe(this.viewLifecycleOwner) {

            val adapterSpinner = ArrayAdapter(
                this.requireContext(), android.R.layout.simple_spinner_item, it.first
            )
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            viewBinding.spinner.adapter = adapterSpinner
            viewBinding.spinner.setSelection(it.second)
        }

        viewBinding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, positionInList: Int, p3: Long) {
                viewModel.changeAccount(viewBinding.spinner.selectedItem.toString(), positionInList)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        viewModel.currentAccountNotesListLD.observe(this.viewLifecycleOwner) {
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
                .navigateSafe(
                    MainFragmentDirections.toNoteDetailsFragment(null)
                )
        }

        viewBinding.btnAccountSetting.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toAccountSettingsFragment())
        }

        viewBinding.btnCloud.setOnClickListener {
            showCloudDialog()
        }
    }

    private fun showCloudDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chose your destiny")
            .setMessage("What do you want to do with your notes?")
            .setPositiveButton("Import") { dialog, _ ->
//                viewBinding.piCircular.isVisible = true
                viewModel.importNotes()
                dialog.cancel()
            }.setNegativeButton("Export") { dialog, _ ->
//                viewBinding.piCircular.isVisible = true
                viewModel.exportNotes()
                dialog.cancel()
            }.show()
    }
}

