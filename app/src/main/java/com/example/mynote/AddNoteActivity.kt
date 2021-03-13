package com.example.mynote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.fragment_notes.*

class AddNoteActivity : AppCompatActivity() {
    var point = false

    companion object{
        const val  TITLE = "title"
        const val  DESCRIPTION = "description"
        const val  POINTED = "point"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setSupportActionBar(findViewById(R.id.bar_title))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Add Note"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_btn -> savebtn()
            R.id.like -> point = if (!point) {
                item.setIcon(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(this,"Added To Your Favorites", Toast.LENGTH_SHORT).show()
                true
            }else{
                item.setIcon(R.drawable.ic_baseline_favorite_border_24)
                Toast.makeText(this,"Removed From Your Favorites", Toast.LENGTH_SHORT).show()
                false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savebtn(){
        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra(TITLE,add_title.text.toString())
        intent.putExtra(DESCRIPTION,add_description.text.toString())
        intent.putExtra(POINTED,point)
        setResult(RESULT_OK,intent)
        finish()
    }
}