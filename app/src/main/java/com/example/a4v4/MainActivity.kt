package com.example.a4v4

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.a4v4.application.MyApp
import com.example.a4v4.databinding.ActivityMainBinding
import com.example.a4v4.ui.details.DetailsFragment
import com.example.a4v4.ui.home.HomeFragment
import com.example.assignment4.application.FileHandler


class MainActivity : AppCompatActivity() {

    lateinit var binding            : ActivityMainBinding


    private val homeFragment    =   HomeFragment()
    private val detailsFragment    =   DetailsFragment()

    private lateinit var myDrawer: MyDrawerLayoutHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.apply {
            overflowIcon = ContextCompat.getDrawable(
                this.context, R.drawable.toolbar_menu_more
            )
            title   =   "Home"
        }

        setSupportActionBar(binding.toolbar)


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, homeFragment)
            commit()
        }

        myDrawer  = MyDrawerLayoutHelper(
            binding.drawerLayout,
            binding.drawerNav,
            supportActionBar!!,
            application,
            applicationContext,
            binding.toolbar
        )
    }

    /*----- POPULATE APP BAR MENU -----*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.export  -> {
                FileHandler(this).createCSV((application as MyApp).repository.myContacts)
                true
            }
            R.id.home   ->  {
                myDrawer.drawerLayout.openDrawer(GravityCompat.START)
                Log.d("drawropen", "onOptionsItemSelected: ")
                return true
            }
            else    -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        if (homeFragment.isVisible) {
            myDrawer.drawerLayout.openDrawer(GravityCompat.START)
            Log.d("drawropen", "onOptionsItemSelected: ")
        }
        else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_container, homeFragment)
                commit()
            }
        }
        return true
    }

    fun openDetailsFragment(){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, detailsFragment)
            addToBackStack(null)
            commit()
        }
    }
}