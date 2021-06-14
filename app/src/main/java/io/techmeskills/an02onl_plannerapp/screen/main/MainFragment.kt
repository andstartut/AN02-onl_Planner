package io.techmeskills.an02onl_plannerapp.screen.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.animation.FabReveal
import io.techmeskills.an02onl_plannerapp.databinding.FragmentMainBinding
import io.techmeskills.an02onl_plannerapp.support.NavigationFragment
import io.techmeskills.an02onl_plannerapp.support.navigateSafe
import io.techmeskills.an02onl_plannerapp.support.setVerticalMargin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainFragment : NavigationFragment<FragmentMainBinding>(R.layout.fragment_main), SearchView.OnQueryTextListener {

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
        viewBinding.topAppBar.setVerticalMargin(marginTop = top)
    }

    @ExperimentalCoroutinesApi
    @SuppressLint("StringFormatMatches", "ShowToast", "ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animation = AlphaAnimation(0.1F, 1F)
        animation.duration = 400
        animation.startOffset = 100
        viewBinding.ivToolbarBackground.setImageResource(getImageId())
        viewBinding.ivToolbarBackground.startAnimation(animation)

        val menu = viewBinding.topAppBar.menu
        val itemSpinner: MenuItem = menu.getItem(TOOLBAR_SPINNER_ITEM)
        val spinner = itemSpinner.actionView as Spinner

        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                viewBinding.recyclerView.scrollToPosition(0)
            }
        })

        viewModel.spinnerDataLD.observe(this.viewLifecycleOwner) {
            val adapterSpinner = ArrayAdapter(
                this.requireContext(), android.R.layout.simple_spinner_item, it.first
            )
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapterSpinner
            spinner.setSelection(it.second)
        }
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, positionInList: Int, p3: Long) {
                viewModel.changeAccount(spinner.selectedItem.toString(), positionInList)
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        viewBinding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.toolbar_menu_accountSettings -> {
                    findNavController().navigateSafe(MainFragmentDirections.toAccountSettingsFragment())
                    true
                }
                R.id.toolbar_menu_cloud -> {
                    showCloudDialog()
                    true
                }
                R.id.toolbar_menu_search -> {
                    val searchItem = menu.findItem(R.id.toolbar_menu_search)
                    val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                    val searchView: SearchView = searchItem?.actionView as SearchView
                    searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                        override fun onQueryTextSubmit(query: String): Boolean {
                            adapter.filter.filter(query)
                            return false
                        }

                        override fun onQueryTextChange(s: String): Boolean {
                            adapter.filter.filter(s)
                            return true
                        }
                    })
                    true
                }
                R.id.toolbar_menu_sort_by_date -> {
                    viewModel.switchOrderingByDate()
                    true
                }
                R.id.toolbar_menu_sort_by_title -> {
                    viewModel.switchOrderingByTitle()
                    true
                }
                else -> false
            }
        }

        viewModel.currentAccountNotesListLD.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.progressIndicatorLD.observe(this.viewLifecycleOwner) {
            if (it) {
                viewBinding.piCircular.isVisible = false
            }
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteWithUndo(adapter.currentList[viewHolder.adapterPosition]) { note ->
                    Snackbar.make(view, R.string.note_removed, Snackbar.LENGTH_LONG)
                        .setAction(R.string.UNDO, View.OnClickListener {
                            viewModel.addNote(note)
                        })
                        .show()
                }
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(viewBinding.recyclerView)

        viewBinding.btnAddNew.setOnClickListener {
            FabReveal(viewBinding.btnAddNew, requireView()).apply {
                start(transaction = {
                    findNavController()
                        .navigateSafe(
                            MainFragmentDirections.toNoteDetailsFragment(null)
                        )
                })
            }

        }
    }

    private fun getImageId(): Int {
        val random = Random().nextInt(3)
        return context?.resources!!.getIdentifier(
            "drawable/toolbar_background_$random",
            null,
            context?.packageName
        )
    }

    @ExperimentalCoroutinesApi
    private fun showCloudDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.cloud)
            .setMessage(R.string.what_to_do_with_notes)
            .setPositiveButton(R.string.import_string) { dialog, _ ->
                viewBinding.piCircular.isVisible = true
                viewModel.importNotes()
                dialog.cancel()
            }.setNegativeButton(R.string.export_string) { dialog, _ ->
                viewBinding.piCircular.isVisible = true
                viewModel.exportNotes()
                dialog.cancel()
            }.show()
    }

    companion object {
        const val TOOLBAR_SPINNER_ITEM = 0
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchItem = menu.findItem(R.id.toolbar_menu_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        TODO("Not yet implemented")
    }
}

