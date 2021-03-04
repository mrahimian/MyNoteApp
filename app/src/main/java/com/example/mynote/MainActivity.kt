package com.example.mynote

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mynote.AddNoteActivity.Companion.DESCRIPTION
import com.example.mynote.AddNoteActivity.Companion.TITLE
import com.example.mynote.adapter.NoteAdapter
import com.example.mynote.database.Note
import com.example.mynote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var noteViewModel : NoteViewModel
    private val ADD_NOTE_REQUEST = 1
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        toolbar.overflowIcon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.WHITE, BlendModeCompat.SRC_ATOP)

        val adapter = NoteAdapter()
        note_recycler_view.adapter = adapter
        note_recycler_view.setHasFixedSize(true)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.getNotes().observe(this, Observer {
            adapter.setNotes(it)
        })

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity , AddNoteActivity::class.java)
            startActivityForResult(intent,ADD_NOTE_REQUEST)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_all_notes -> noteViewModel.deleteAllNotes()
            
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            val title = data?.getStringExtra(TITLE)
            val description = data?.getStringExtra(DESCRIPTION)
            val note = Note(title,description)
            noteViewModel.insert(note)
        }
    }


}