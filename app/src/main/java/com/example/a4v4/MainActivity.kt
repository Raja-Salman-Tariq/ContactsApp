package com.example.a4v4

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.a4v4.application.MyApp
import com.example.a4v4.databinding.ActivityMainBinding
import com.example.a4v4.ui.calllogs.CallLogsFragment
import com.example.a4v4.ui.details.DetailsFragment
import com.example.a4v4.ui.files.FilesFragment
import com.example.a4v4.ui.home.HomeFragment
import com.example.a4v4.utils.FileHandler
import com.example.a4v4.utils.MyDrawerLayoutHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var binding            : ActivityMainBinding


    var homeFragment       :   HomeFragment?    =null//=   HomeFragment(this)
    private val detailsFragment    =   DetailsFragment()

    lateinit var myDrawer: MyDrawerLayoutHelper

    lateinit var callLogsFragment    :   CallLogsFragment

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
                (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                    ?.hideSoftInputFromWindow(findViewById<ViewGroup>(android.R.id.content).rootView.windowToken, 0)
                true
            }
            R.id.home   ->  {
                if (homeFragment?.isVisible!!) {
                    myDrawer.drawerLayout.openDrawer(GravityCompat.START)
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                        ?.hideSoftInputFromWindow(
                            findViewById<ViewGroup>(android.R.id.content).rootView.windowToken,
                            0
                        )
                }
                else if (callLogsFragment?.isVisible!!){
                    setActionBarTitle("Details")
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frag_container, detailsFragment)
                        commit()
                    }
                }
                return true
            }
            R.id.history    ->  {
                setActionBarTitle("History")
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frag_container, FilesFragment()!!)
                    commit()
                }
                (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                    ?.hideSoftInputFromWindow(findViewById<ViewGroup>(android.R.id.content).rootView.windowToken, 0)
                return true
            }
            R.id.calllog    ->  {
                setActionBarTitle("Call Log")
//                callLogsFragment = CallLogsFragment((application as MyApp).repository.selectedContact!!)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frag_container, callLogsFragment)
                    commit()
                }
                (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                    ?.hideSoftInputFromWindow(findViewById<ViewGroup>(android.R.id.content).rootView.windowToken, 0)
                return true
            }
            else    -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        if (callLogsFragment.isVisible){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_container, detailsFragment)
                commit()
            }
        }

        if (homeFragment?.isVisible!!) {
            myDrawer.drawerLayout.openDrawer(GravityCompat.START)
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(findViewById<ViewGroup>(android.R.id.content).rootView.windowToken, 0)
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
            homeFragment = HomeFragment()
            callLogsFragment = CallLogsFragment((application as com.example.a4v4.application.MyApp).repository.getSelectedContact())
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
                homeFragment = HomeFragment()
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