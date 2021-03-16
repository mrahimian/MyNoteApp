package com.example.mynote

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.adapter.NoteAdapter
import com.example.mynote.database.Note
import com.example.mynote.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.all_notes_fragment.*

open class AllNotesFragment : Fragment() {
    lateinit var noteViewModel: NoteViewModel
    lateinit var adapter: NoteAdapter
    private val ADD_NOTE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.all_notes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        setAdapter(note_recycler_view)
        noteViewModel.getNotes().observe(this.viewLifecycleOwner, {
            adapter.submitList(it as MutableList<Note>?)
            adapter.filteredNotes = it
        })
        adapterListener(1)
        setItemTouchHelperCallback(note_recycler_view)

        fab.setOnClickListener {
            val intent = Intent(this.activity, AddNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        cancel_button.setOnClickListener {
            finishDeleting()
        }

        delete_button.setOnClickListener {
            val deleted = mutableListOf<Note>()
            for (note in noteViewModel.getNotes().value!!){
                if (note.isSelected){
                    deleted.add(note)
                }
            }
            noteViewModel.deleteSelected()
            finishDeleting()
            snackbarJobs(view , deleted)
        }

        checkboxBar.setOnCheckedChangeListener { compoundButton, bool ->
            if (bool) {
                selectAllItems(true, check = true)
            } else {
                selectAllItems(false, check = false)
            }
        }
    }

    fun finishDeleting(){
        delete_bar.animate().translationY(500F).duration = 500
        adapter.deleteMode = false
        selectAllItems(false, check = false)
        adapter.selectedItems = 0
        checkboxBar.isSelected = false
    }

    fun selectAllItems(select: Boolean, check: Boolean) {
        val count: Int = note_recycler_view.childCount
        for (i in 0 until count) {
            val view: View = note_recycler_view.getChildAt(i)
            val selectMode: ImageView = view.findViewById(R.id.note_select_mode)
            val checkedMode: ImageView = view.findViewById(R.id.note_selected)
            val note = adapter.getNoteAt(i)
            if (select) {
                if (!check) else adapter.selectItem(
                    checkedMode,
                    view,
                    note
                )
            } else {
                adapter.deselectItem(checkedMode, view, note)
                selectMode.visibility = View.INVISIBLE
            }
            Log.e("it",note.toString())
        }
    }


    /**
     * we have this function because in FavoriteFragment we just want to call Fragment onViewCreated method
     */
    fun callSuper(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * set the adapter
     */
    fun setAdapter(view: RecyclerView) {
        adapter = NoteAdapter(noteViewModel)
        view.adapter = adapter
        view.setHasFixedSize(true)
    }

    fun adapterListener(mainOrFragment: Int) {
        adapter.setOnClickListener(object : NoteAdapter.OnClickListener {
            override fun onClick(note: Note) {
                val title = note.title.toString()
                val description = note.text.toString()
                val id = note.id
                val action = id?.let {
                    if (mainOrFragment == 1) {
                        AllNotesFragmentDirections.actionAllNotesFragmentToNotesFragment(note)
                    } else {
                        FavoriteFragmentDirections.actionFavoriteFragmentToNotesFragment(note)
                    }
                }
                if (action != null) {
                    findNavController().navigate(action)
                }
            }

            override fun onLongClick(note: Note) {
                animate()
            }

            override fun select(numberSelected: Int) {
                item_selected_number.text = "$numberSelected Items Selected"
            }
        })
    }

    /**
     * do item touch helper works
     */
    open fun setItemTouchHelperCallback(view: RecyclerView) {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val note = adapter.getNoteAt(viewHolder.adapterPosition)
                    noteViewModel.delete(note)
                    snackbarJobs(viewHolder.itemView, listOf(note))
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(view)
    }

    fun snackbarJobs(view:View, notes : List<Note>)  {
        val snackbar =
            Snackbar.make(view, "Item(s) Removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    undoAction(notes)
                }.apply {
                    setActionTextColor(Color.RED)
                }
        val snackView = snackbar.view
        val textView =
            snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.YELLOW)
        snackbar.show()
    }

    private fun undoAction(notes : List<Note>){
        for (note in notes){
            noteViewModel.insert(note)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            val title = data?.getStringExtra(AddNoteActivity.TITLE)
            val description = data?.getStringExtra(AddNoteActivity.DESCRIPTION)
            val point = if (data?.getBooleanExtra(AddNoteActivity.POINTED, false) == true) {
                1
            } else {
                0
            }
            val note = Note(title, description, point)
            noteViewModel.insert(note)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView
        // Configure the search info and add any event listeners...
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText);
                return false;
            }
        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes -> noteViewModel.deleteAllNotes()
            R.id.delete -> {
                selectAllItems(true, false)
                adapter.deleteMode = true
                animate()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun animate() = run { delete_bar.animate().translationY(0F).duration = 0}


}