package com.manga.mangahubapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.LoginRequest
import com.manga.mangahubapp.model.LoginResponse
import com.manga.mangahubapp.network.ApiRepositoryImpl
import com.manga.mangahubapp.util.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPage : AppCompatActivity() {

    private val activity: AppCompatActivity = this@LoginPage
    private val apiRepository = ApiRepositoryImpl()
    private val validator = Validator()
    private var username: TextInputEditText? = null
    private var password: TextInputEditText? = null
    private var usernameContainer: TextInputLayout? = null
    private var passwordContainer: TextInputLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }


    private fun init() {
        username = findViewById<TextInputEditText>(R.id.loginEditText)
        password = findViewById<TextInputEditText>(R.id.passwordEditText)

        usernameContainer = findViewById<TextInputLayout>(R.id.loginContainer)
        passwordContainer = findViewById<TextInputLayout>(R.id.passwordContainer)

        username?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                usernameContainer?.let { c -> c.helperText = validateUsername() }
            }
        }
        password?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                passwordContainer?.let { c -> c.helperText = validatePassword() }
            }
        }
    }

    fun registration(view: View) {
        val intent = Intent(activity, RegisterPage::class.java)
        startActivity(intent)
        activity.finish()
    }

    fun forgotPassword(view: View) {
        val intent = Intent(activity, ForgotPassword::class.java)
        startActivity(intent)
        activity.finish()
    }


    private fun validateUsername(): String {
        return validator.validateLogin(username?.text.toString().trim())
    }

    private fun validatePassword(): String {
        return validator.validatePassword(password?.text.toString().trim())
    }


    fun login(view: View) {

        val user = LoginRequest(username?.text.toString().trim(), password?.text.toString().trim())

        apiRepository.login(
            user,
            object :
                Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val tokens = response.body()
                        if (tokens != null) {
                            val intent = Intent(activity, MainPage::class.java)
                            startActivity(intent)
                            activity.finish()
                        } else {
                            AlertDialog.Builder(activity)
                                .setTitle("Sing in")
                                .setMessage("Something went wrong. Try again later.")
                                .setPositiveButton("Okay") { _, _ ->
                                    val intent = Intent(activity, LoginPage::class.java)
                                    startActivity(intent)
                                    activity.finish()
                                }
                                .show()
                        }
                    } else {
                        AlertDialog.Builder(activity)
                            .setTitle("Sing in")
                            .setMessage("Something went wrong. Try again later.")
                            .setPositiveButton("Okay") { _, _ ->
                                val intent = Intent(activity, LoginPage::class.java)
                                startActivity(intent)
                                activity.finish()
                            }
                            .show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("Error", t.message.toString())
                    AlertDialog.Builder(activity)
                        .setTitle("Sing in")
                        .setMessage("Something went wrong. Try again later.")
                        .setPositiveButton("Okay") { _, _ ->
                            val intent = Intent(activity, LoginPage::class.java)
                            startActivity(intent)
                            activity.finish()
                        }
                        .show()
                }
            })
    }

}