package com.manga.mangahubapp.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manga.mangahubapp.R


class MainPage : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    var bottomNavigationView: BottomNavigationView? = null
    private val activity: BottomNavigationView.OnNavigationItemSelectedListener = this@MainPage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomMenu);

        bottomNavigationView?.let { b ->
            b.setOnNavigationItemSelectedListener(activity)
            b.setSelectedItemId(R.id.home)
            b.setItemIconTintList(null)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.Fragment,
                    MenuPage()
                ).commit()
                return true
            }

            R.id.home -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.Fragment,
                    HomePage()
                ).commit()
                return true
            }

            R.id.profile -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.Fragment,
                    ProfilePage()
                ).commit()
                return true
            }

            R.id.create -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.Fragment,
                    CreatePage()
                ).commit()
                return true
            }
        }
        return false
    }
}

