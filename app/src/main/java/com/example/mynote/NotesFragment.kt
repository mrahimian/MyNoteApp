package com.example.mynote

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.mynote.database.Note
import com.example.mynote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.fragment_notes.*

class NotesFragment : Fragment(R.layout.fragment_notes) {
    val args: NotesFragmentArgs by navArgs()
    private lateinit var noteViewModel : NoteViewModel
    private lateinit var note : Note
    var pointed : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        setHasOptionsMenu(true)
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
//        toolbar?.inflateMenu(R.menu.save_bar)
        /*toolbar?.setOnMenuItemClickListener {
            if (it.itemId == R.id.save_btn){
                noteViewModel.update(note)
            }
            true
        }*/
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_bar, menu);
        if (pointed == 0){
            menu.findItem(R.id.like).setIcon(R.drawable.ic_baseline_favorite_border_24)
        }else{
            menu.findItem(R.id.like).setIcon(R.drawable.ic_baseline_favorite_24)
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_btn -> {
                val title = edit_title.text.toString()
                val description = edit_description.text.toString()
                note.title = title
                note.text = description
                note.pointed = pointed
                noteViewModel.update(note)

                val action = NotesFragmentDirections.actionNotesFragmentToAllNotesFragment()
                findNavController().navigate(action)
            }
            R.id.like -> {
                if (pointed == 0) {
                    item.setIcon(R.drawable.ic_baseline_favorite_24)
                    pointed = 1
                    Log.e("point2",note.pointed.toString())
                    Toast.makeText(activity,"Added To Your Favorites",Toast.LENGTH_SHORT).show()
                }else{
                    item.setIcon(R.drawable.ic_baseline_favorite_border_24)
                    pointed = 0
                    Log.e("point3",note.pointed.toString())
                    Toast.makeText(activity,"Removed From Your Favorites",Toast.LENGTH_SHORT).show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        note = args.note
        pointed = note.pointed

        edit_title.setText(note.title)
        edit_description.setText(note.text)



        /*val toolbar = view.findViewById<Toolbar>(R.id.bar_title)
        toolbar.inflateMenu(R.menu.save_bar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = "Edit Note"
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
        actionBar?.setDisplayHomeAsUpEnabled(true)*/
//        val toolbar = bar_title
//        val navHostFragment = NavHostFragment.findNavController(this);
//        Log.e("toolbar", bar_title.toString())
//        NavigationUI.setupWithNavController(toolbar, navHostFragment)
    }
}