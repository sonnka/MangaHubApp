package com.manga.mangahubapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.LoginResponse
import com.manga.mangahubapp.network.ApiRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPage : AppCompatActivity() {

    private val activity: AppCompatActivity = this@LoginPage
    private val apiRepository = ApiRepositoryImpl()
    private var username: EditText? = null
    private var password: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(3_000)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init();
    }

    private fun init() {
        username = findViewById<EditText>(R.id.loginInput)
        password = findViewById<EditText>(R.id.passwordInput)
    }

    fun registration(view: View) {
        val intent = Intent(activity, RegisterPage::class.java)
        startActivity(intent)
        activity.finish()
    }

    fun login(view: View) {
        apiRepository.login(
            username?.text.toString().trim(),
            password?.text.toString().trim(),
            object :
                Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val tokens = response.body()
                    if (tokens != null) {
                        val intent = Intent(activity, MainPage::class.java)
                        startActivity(intent)
                        activity.finish()
                    } else {
                        Toast.makeText(activity, "Tokens are null!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("Tag", t.message.toString())
                    Toast.makeText(activity, "Server error : " + t.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
}