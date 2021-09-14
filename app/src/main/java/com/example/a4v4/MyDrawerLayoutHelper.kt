package com.example.a4v4

import android.app.Application
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.a4v4.application.MyApp
import com.example.a4v4.database.DummyModel
import com.google.android.material.navigation.NavigationView

class MyDrawerLayoutHelper(
    val drawerLayout        :   DrawerLayout,
    val navigationView      :   NavigationView,
    val actionBar           :   ActionBar,
    val application         :   Application,
    val applicationContext  :   Context,
    val toolbar             :   Toolbar,
    ){

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
                    (application as MyApp).repository.setContacts(0)
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
                    (application as MyApp).repository.setContacts(DummyModel.TYPE_JAZZ)
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
                    (application as MyApp).repository.setContacts(DummyModel.TYPE_TELENOR)
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
                    (application as MyApp).repository.setContacts(DummyModel.TYPE_UFONE)
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
                    (application as MyApp).repository.setContacts(DummyModel.TYPE_ZONG)
                }
            }

            true
        }
    }
}