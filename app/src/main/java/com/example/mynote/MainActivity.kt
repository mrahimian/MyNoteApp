package com.example.mynote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mynote.adapter.NoteAdapter
import com.example.mynote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var noteViewModel : NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val adapter = NoteAdapter()
        note_recycler_view.adapter = adapter
        note_recycler_view.setHasFixedSize(true)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.getNotes().observe(this, Observer {
            adapter.setNotes(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu)
    }
}