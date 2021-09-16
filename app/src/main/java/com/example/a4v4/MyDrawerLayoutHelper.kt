package com.example.a4v4

import android.app.Application
import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.a4v4.database.ContactsModel
import com.example.a4v4.ui.apps.AppsFragment
import com.google.android.material.navigation.NavigationView

class MyDrawerLayoutHelper(
    private val mainActivity        : MainActivity,
    val drawerLayout                :   DrawerLayout,
    private val navigationView      :   NavigationView,
    private val actionBar           :   ActionBar,
    val application                 :   Application,
    private val applicationContext  :   Context,
    private val toolbar             :   Toolbar,
    ){

    var selectedTitle   =   "Home"

    init {
        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            drawerLayout.closeDrawers()

            when (it.itemId) {
                R.id.nav_home -> {
                    actionBar?.title = "Home"
                    actionBar?.setBackgroundDrawable(
                        ColorDrawable(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.blue
                            )
                        )
                    )
                    toolbar.setTitleTextColor(
                        ContextCompat.getColor(applicationContext, R.color.black_alt)
                    )
                    mainActivity.homeFragment?.getContacts(0)
                    selectedTitle="Home"
                }
                R.id.nav_jazz -> {
                    actionBar?.title = "Jazz"
                    actionBar?.setBackgroundDrawable(
                        ColorDrawable(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.jazz_red
                            )
                        )
                    )
                    toolbar.setTitleTextColor(
                        ContextCompat.getColor(applicationContext, R.color.jazz_yellow)
                    )
                    mainActivity.homeFragment?.getContacts(ContactsModel.TYPE_JAZZ)
                    selectedTitle="Jazz"
                }
                R.id.nav_telenor -> {
                    actionBar?.title = "Telenor"
                    actionBar?.setBackgroundDrawable(
                        ColorDrawable(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.telenor_blue
                            )
                        )
                    )
                    toolbar.setTitleTextColor(
                        ContextCompat.getColor(applicationContext, R.color.black_alt)
                    )
                    mainActivity.homeFragment?.getContacts(ContactsModel.TYPE_TELENOR)
                    selectedTitle="Telenor"
                }
                R.id.nav_ufone -> {
                    actionBar?.title = "Ufone"
                    actionBar?.setBackgroundDrawable(
                        ColorDrawable(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.ufone_orange
                            )
                        )
                    )
                    toolbar.setTitleTextColor(
                        ContextCompat.getColor(applicationContext, R.color.black)
                    )
                    mainActivity.homeFragment?.getContacts(ContactsModel.TYPE_UFONE)
                    selectedTitle="Ufone"
                }
                R.id.nav_zong -> {
                    actionBar?.title = "Zong"
                    actionBar?.setBackgroundDrawable(
                        ColorDrawable(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.zong_green
                            )
                        )
                    )
                    toolbar.setTitleTextColor(
                        ContextCompat.getColor(applicationContext, R.color.zong_purple)
                    )
                    mainActivity.homeFragment?.getContacts(ContactsModel.TYPE_ZONG)
                    selectedTitle="Zong"
                }
                R.id.nav_other -> {
                    actionBar?.title = "Other"
                    actionBar?.setBackgroundDrawable(
                        ColorDrawable(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.jazz_yellow
                            )
                        )
                    )
                    toolbar.setTitleTextColor(
                        ContextCompat.getColor(applicationContext, R.color.black_alt)
                    )
                    mainActivity.homeFragment?.getContacts(ContactsModel.TYPE_OTHER)
                    selectedTitle="Other"
                }
                R.id.nav_apps   ->  {
                    actionBar?.title = "Apps"
//                    actionBar?.setBackgroundDrawable(
//                        ColorDrawable(
//                            ContextCompat.getColor(
//                                applicationContext,
//                                R.color.jazz_red
//                            )
//                        )
//                    )
//                    toolbar.setTitleTextColor(
//                        ContextCompat.getColor(applicationContext, R.color.jazz_yellow)
//                    )
                    mainActivity.supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frag_container, AppsFragment(mainActivity))
                        addToBackStack(null)
                        commit()
                    }
                }
            }

            true
        }
    }
}