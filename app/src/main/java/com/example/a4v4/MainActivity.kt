package com.example.a4v4

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.view.*
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
import androidx.core.view.MenuItemCompat


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var    binding             :   ActivityMainBinding

    var             homeFragment        :   HomeFragment?    =null//=   HomeFragment(this)
    private val     detailsFragment     =   DetailsFragment()
    lateinit var    callLogsFragment    :   CallLogsFragment

    lateinit var    myDrawer            :   MyDrawerLayoutHelper
    var             allContactsLoaded   :   Boolean         =   false
    private lateinit var menu: Menu

    /*============================================================================================*/

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
            application as MyApp,
            applicationContext,
            binding.toolbar
        )
    }

    fun handleLoading(isLoading :   Boolean){
        if (isLoading){
            Log.d("loadarr", "onCreate: tru")
            binding.myLoader.visibility=View.VISIBLE
            binding.mainLoadingFocus.visibility=View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else{
            Log.d("loadarr", "onCreate: false")
            binding.myLoader.visibility=View.GONE
            binding.mainLoadingFocus.visibility=View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    fun setActionBarTitle(str:String){
        binding.toolbar.apply {
            title   =   str
        }

    }

    /*==============================================================================================
    * ---------------------------     S E A R C H   V I E W     ---------------------------------- *
    ==============================================================================================*/

    override fun onQueryTextSubmit(query: String?): Boolean {
        homeFragment?.myRvAdapter?.filter?.filter(query)
        (menu.findItem(R.id.menu_search).actionView as SearchView).clearFocus()
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        homeFragment?.myRvAdapter?.filter?.filter(newText)
        return false
    }

    /*==============================================================================================
    * ---------------------------     O P T I O N S   M E N U   ---------------------------------- *
    ==============================================================================================*/

    /*----- POPULATE APP BAR MENU -----*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        this.menu   =   menu

        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            setOnQueryTextListener(this@MainActivity)
            setOnQueryTextFocusChangeListener { _, p1 ->
                if (!p1)// not have focus
                    isIconified =   true
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Log.d("onoptionselect", "onOptionsItemSelected: ")

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
//                    Log.d("allLoaded", "onOptionsItemSelected: ")
//                    if (allContactsLoaded) {
//                        myDrawer.drawerLayout.openDrawer(GravityCompat.START)
//                        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
//                            ?.hideSoftInputFromWindow(
//                                findViewById<ViewGroup>(android.R.id.content).rootView.windowToken,
//                                0
//                            )
//                    }
//                    else{
//                        Snackbar
//                            .make(
//                                binding.root,
//                                "Please wait while all contacts are imported.",
//                                Snackbar.LENGTH_INDEFINITE
//                            )
//                            .setAction("Close App"){}
//                            .show()
//                    }
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
                    addToBackStack(null)
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

    /*==============================================================================================
    * ---------------------------     N A V I G A T I O N S     ---------------------------------- *
    ==============================================================================================*/

    override fun onBackPressed() {

        when {
            callLogsFragment.isResumed -> {
                Log.d("onsportnav", "onSupportNavigateUp: by call logs")
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frag_container, detailsFragment)
                    commit()
                }
            }
            detailsFragment.isVisible -> {
                supportFragmentManager.popBackStack()
            }
            homeFragment?.isResumed!! && !myDrawer.drawerLayout.isDrawerOpen(GravityCompat.START)-> {
                Log.d("onsportnav", "onSupportNavigateUp: by home")
                myDrawer.drawerLayout.openDrawer(GravityCompat.START)
                (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                    ?.hideSoftInputFromWindow(
                        findViewById<ViewGroup>(android.R.id.content).rootView.windowToken,
                        0
                    )
            }

            else    ->
                super
                    .onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        when {
            callLogsFragment.isResumed -> {
                Log.d("onsportnav", "onSupportNavigateUp: by call logs")
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frag_container, detailsFragment)
                    commit()
                }
            }
            detailsFragment.isVisible -> {
                supportFragmentManager.popBackStack()
            }
            homeFragment?.isResumed!! -> {
                if (allContactsLoaded) {
                    Log.d("onsportnav", "onSupportNavigateUp: by home")
                    myDrawer.drawerLayout.openDrawer(GravityCompat.START)
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                        ?.hideSoftInputFromWindow(
                            findViewById<ViewGroup>(android.R.id.content).rootView.windowToken,
                            0
                        )
                }
                else{
                    Snackbar
                        .make(
                            binding.root,
                            "Please wait while all contacts are imported.",
                            Snackbar.LENGTH_INDEFINITE
                        )
                        .setAction("OK"){}
                        .show()
                }
            }
            else    ->  {
                Log.d("onoptionselect", "onSupportNavigateUp: ")
                supportFragmentManager.popBackStack()
                if (myDrawer.navigationView.menu.findItem(R.id.nav_apps).isChecked)
                    myDrawer.reset()
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

    /*==============================================================================================
    * ---------------------------     P E R M I S S I O N S     ---------------------------------- *
    ==============================================================================================*/
    private fun getPermission(mainActivity: MainActivity){
        val permission: String = Manifest.permission.READ_CONTACTS
        val grant = ContextCompat.checkSelfPermission(mainActivity, permission)
        val grant2= ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_CALL_LOG)
        if (grant == PackageManager.PERMISSION_GRANTED && grant2 == PackageManager.PERMISSION_GRANTED ) {
            (application as MyApp).initRepo()
            homeFragment = HomeFragment()
            callLogsFragment = CallLogsFragment((application as com.example.a4v4.application.MyApp).repository.getSelectedContact())
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frag_container, homeFragment!!)
                commit()
            }
        }
        else{

            var permissionList =   arrayOfNulls<String>(1)


            if (grant == grant2  && grant != PackageManager.PERMISSION_GRANTED) {
                permissionList = arrayOfNulls<String>(2)
                permissionList[0] = permission
                permissionList[1] = Manifest.permission.READ_CALL_LOG
            }


            else if (grant!= PackageManager.PERMISSION_GRANTED) {
                permissionList = arrayOfNulls<String>(1)
                permissionList[0] = permission
            }

            else if (grant2!= PackageManager.PERMISSION_GRANTED) {
                permissionList = arrayOfNulls<String>(1)
                permissionList[0] = Manifest.permission.READ_CALL_LOG
            }

            ActivityCompat.requestPermissions(mainActivity, permissionList, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==  PackageManager.PERMISSION_GRANTED) {
                (application as MyApp).initRepo()
                homeFragment = HomeFragment()
                callLogsFragment = CallLogsFragment((application as com.example.a4v4.application.MyApp).repository.getSelectedContact())
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frag_container, homeFragment!!)
                    commit()
                }
            }
            else {
                Snackbar
                    .make(
                        binding.root,
                        "Please grant the required permissions to use this app.",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    .setAction("Close App") {
                        finish()
                    }
                    .show()
            }
        }
    }
}