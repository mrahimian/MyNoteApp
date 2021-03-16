package com.example.mynote

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.database.Note
import com.example.mynote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlin.system.exitProcess

class FavoriteFragment : AllNotesFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.callSuper(view, savedInstanceState)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        setAdapter(favorites_note_recycler_view)

        noteViewModel.getPointedNotes().observe(this.viewLifecycleOwner, {
            adapter.submitList(it as MutableList<Note>?)
            adapter.filteredNotes = it
        })

        adapterListener(2)

        setItemTouchHelperCallback(favorites_note_recycler_view)
    }

    override fun setItemTouchHelperCallback(view: RecyclerView) {
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
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Are You Sure You Want To Remove This Item From Favorites")
                    builder.apply {
                        setPositiveButton("Yes"
                        ) { dialog, id ->
                            val note = adapter.getNoteAt(viewHolder.adapterPosition)
                            note.pointed = 0
                            noteViewModel.update(note)
//                                adapter.submitList()
                        }
                        setNegativeButton("No"
                        ) { dialog, id ->
                            adapter.notifyDataSetChanged()
                        }
                    }
                    builder.create()
                    builder.show()
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(view)
    }


}