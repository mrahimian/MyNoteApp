package com.example.mynote

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.mynote.viewmodel.NoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //change three dots color to white
        toolbar.overflowIcon?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color.WHITE,
                BlendModeCompat.SRC_ATOP
            )
        setSupportActionBar(findViewById(R.id.toolbar))


        //bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.allNotesFragment, R.id.favoriteFragment))

        bottomNav.setupWithNavController( navController);
        setupActionBarWithNavController(navController,appBarConfiguration);

/*

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.all_notes_fragment) {
                toolbar.visibility = View.GONE
            }else {
                toolbar.visibility = View.VISIBLE
            }
        }
*/
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
    }


    override fun onSupportNavigateUp(): Boolean {
        Log.e("clicked1", "")
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}