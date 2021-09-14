package com.example.a4v4

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.a4v4.application.MyApp
import com.example.a4v4.databinding.ActivityMainBinding
import com.example.a4v4.ui.details.DetailsFragment
import com.example.a4v4.ui.home.HomeFragment
import com.example.a4v4.application.FileHandler


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var binding            : ActivityMainBinding


    /*lateinit*/ var homeFragment       :   HomeFragment    =   HomeFragment(this)
    private val detailsFragment    =   DetailsFragment()

    lateinit var myDrawer: MyDrawerLayoutHelper


//    override fun onResume() {
//        super.onResume()
//        homeFragment = HomeFragment(this)
//    }

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
            this,
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

        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            setOnQueryTextListener(this@MainActivity)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.export  -> {
                FileHandler(this).createCSV((application as MyApp).repository?.allContacts?.value)
                true
            }
            R.id.home   ->  {
                myDrawer.drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
            else    -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        if (homeFragment.isVisible) {
            myDrawer.drawerLayout.openDrawer(GravityCompat.START)
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        homeFragment.myRvAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        homeFragment.myRvAdapter.filter.filter(newText)
        return false
    }

    fun setActionBarTitle(str:String){
        binding.toolbar.apply {
            title   =   str
        }

    }


}