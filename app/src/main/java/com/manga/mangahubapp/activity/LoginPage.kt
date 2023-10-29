package com.manga.mangahubapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.manga.mangahubapp.R

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun registration(view: View) {
        val intent = Intent(this, RegisterPage::class.java)
        startActivity(intent)
        this.finish()
    }
}