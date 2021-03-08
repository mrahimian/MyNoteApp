package com.example.mynote

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.adapter.NoteAdapter
import com.example.mynote.database.Note
import com.example.mynote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var noteViewModel : NoteViewModel
    private val ADD_NOTE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NoteAdapter()
        note_recycler_view.adapter = adapter
        note_recycler_view.setHasFixedSize(true)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.getNotes().observe(this.viewLifecycleOwner, {
            adapter.setNotes(it)
        })

        adapter.setOnClickListener(object : NoteAdapter.OnClickListener{
            override fun onClick(note: Note) {
                val title = note.title.toString()
                val description = note.text.toString()
                val id = note.id
                val action = id?.let {
                    MainFragmentDirections.actionEditNoteFragmentToNotesFragment(title, description,
                        it
                    )
                }
                if (action != null) {
                    findNavController().navigate(action)
                }
            }
        })

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(note_recycler_view)

        fab.setOnClickListener {
            val intent = Intent(this.activity , AddNoteActivity::class.java)
            startActivityForResult(intent,ADD_NOTE_REQUEST)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == AppCompatActivity.RESULT_OK){
            val title = data?.getStringExtra(AddNoteActivity.TITLE)
            val description = data?.getStringExtra(AddNoteActivity.DESCRIPTION)
            val note = Note(title,description)
            noteViewModel.insert(note)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_all_notes -> noteViewModel.deleteAllNotes()

        }
        return super.onOptionsItemSelected(item)
    }



}