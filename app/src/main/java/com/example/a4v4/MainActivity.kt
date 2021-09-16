package com.example.a4v4

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.a4v4.application.MyApp
import com.example.a4v4.databinding.ActivityMainBinding
import com.example.a4v4.ui.details.DetailsFragment
import com.example.a4v4.ui.files.FilesFragment
import com.example.a4v4.ui.home.HomeFragment
import com.example.a4v4.utils.FileHandler
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var binding            : ActivityMainBinding


    var homeFragment       :   HomeFragment?    =null//=   HomeFragment(this)
    private val detailsFragment    =   DetailsFragment()

    lateinit var myDrawer: MyDrawerLayoutHelper


//    override fun onResume() {
//        super.onResume()
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPermission(this)

        binding.toolbar.apply {
            overflowIcon = ContextCompat.getDrawable(
                this.context, R.drawable.toolbar_menu_more
            )
            title   =   "Home"
        }

        setSupportActionBar(binding.toolbar)


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
                FileHandler(this).createCSV((application as MyApp).repository?.allContacts?.value).myFile.run {
                    if (this!=null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            (application as MyApp).repository.insertFile(this@run)
                        }
                    }
                }
                true
            }
            R.id.home   ->  {
                myDrawer.drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
            R.id.history    ->  {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frag_container, FilesFragment(this@MainActivity)!!)
                    commit()
                }
                return true
            }
            else    -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        if (homeFragment?.isVisible!!) {
            myDrawer.drawerLayout.openDrawer(GravityCompat.START)
        }
        else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_container, homeFragment!!)
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
        homeFragment?.myRvAdapter?.filter?.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        homeFragment?.myRvAdapter?.filter?.filter(newText)
        return false
    }

    fun setActionBarTitle(str:String){
        binding.toolbar.apply {
            title   =   str
        }

    }

    private fun getPermission(mainActivity: MainActivity){
        val permission: String = Manifest.permission.READ_CONTACTS
        val grant = ContextCompat.checkSelfPermission(mainActivity, permission)
        if (grant == PackageManager.PERMISSION_GRANTED) {
            (application as MyApp).initRepo()
            homeFragment = HomeFragment(this)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_container, homeFragment!!)
                commit()
            }
        }
        else{
            val permissionList = arrayOfNulls<String>(1)
            permissionList[0] = permission
            ActivityCompat.requestPermissions(mainActivity, permissionList, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                (application as MyApp).initRepo()
                homeFragment = HomeFragment(this)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frag_container, homeFragment!!)
                    commit()
                }
            } else {
                Snackbar
                    .make(binding.root, "Please grant permission for contacts to use this app.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Close App") {
                        finish()
                    }
                    .show()
            }
        }
    }
}