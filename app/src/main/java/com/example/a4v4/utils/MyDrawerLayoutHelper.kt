package com.example.a4v4.utils

import android.app.Application
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.example.a4v4.application.MyApp
import com.example.a4v4.database.ContactsModel
import com.example.a4v4.ui.apps.AppsFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar

class MyDrawerLayoutHelper(
    private val mainActivity        : MainActivity,
    val drawerLayout                :   DrawerLayout,
    val navigationView              :   NavigationView,
    private val actionBar           :   ActionBar,
    val application                 :   MyApp,
    private val applicationContext  :   Context,
    private val toolbar             :   Toolbar,
    ){

    var selectedTitle   =   "Home"

    init {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        application.repository.allContactsLoaded.observe(mainActivity){

            // it == loaded

            Log.d("allLoaded", "all loaded: : $it ")
            mainActivity.allContactsLoaded=it
            if (it) {
                mainActivity.binding.myBgLoader.visibility = View.GONE
            }
            else
                Snackbar.make(mainActivity.binding.root,
                "Some functionality such as searching will be available after all contacts " +
                        "have been imported. This loading is only needed once when either the app is" +
                        " installed, or new data has been found.",
                LENGTH_INDEFINITE).apply {
                    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                        .maxLines = 5
                    setAction("OK") {}
                    show()
                }

        }

        navigationView.itemIconTintList = null;
        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            drawerLayout.closeDrawers()
            mainActivity.homeFragment?.run{
                myRvAdapter?.resetPages()
                recyclerView.scrollToPosition(0)
            }

            when (it.itemId) {
                R.id.nav_home -> {
                    setScreenState("Home", R.color.blue, R.color.black_alt, 0)
                }
                R.id.nav_jazz -> {
                    setScreenState("Jazz", R.color.jazz_red, R.color.jazz_yellow, ContactsModel.TYPE_JAZZ)
                }
                R.id.nav_telenor -> {
                    setScreenState("Telenor", R.color.telenor_blue, R.color.black_alt, ContactsModel.TYPE_TELENOR)
                }
                R.id.nav_ufone -> {
                    setScreenState("UFone", R.color.ufone_orange, R.color.black, ContactsModel.TYPE_UFONE)
                }
                R.id.nav_zong -> {
                    setScreenState("Zong", R.color.zong_green, R.color.zong_purple, ContactsModel.TYPE_ZONG)
                }
                R.id.nav_other -> {
                    setScreenState("Other", R.color.jazz_yellow, R.color.black_alt, ContactsModel.TYPE_OTHER)
                }
                R.id.nav_apps ->  {
                    actionBar?.title = "Apps"
                    mainActivity.supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frag_container, AppsFragment())
                        addToBackStack(null)
                        commit()
                    }
                }
            }
            true
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    private fun setScreenState(title:String, color:Int, titleColor:Int, contactType:Short){

        actionBar?.title = title
        actionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    applicationContext,
                    color
                )
            )
        )
        toolbar.setTitleTextColor(
            ContextCompat.getColor(applicationContext, titleColor)
        )
        mainActivity.homeFragment?.getContacts(contactType)
        selectedTitle=title
    }

    fun reset() {

        val itemId  =   when(selectedTitle){
            "Jazz"      ->  R.id.nav_jazz
            "Telenor"   ->  R.id.nav_telenor
            "UFone"     ->  R.id.nav_ufone
            "Zong"      ->  R.id.nav_zong
            "Other"     ->  R.id.nav_other
            else        ->  R.id.nav_home
        }

        navigationView.menu.findItem(itemId).isChecked=true
    }
}