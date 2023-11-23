package com.manga.mangahubapp.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.ForgotPasswordRequest
import com.manga.mangahubapp.network.ApiRepositoryImpl
import com.manga.mangahubapp.util.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : AppCompatActivity() {

    private val activity: AppCompatActivity = this@ForgotPassword
    private val apiRepository = ApiRepositoryImpl()
    private val validator = Validator()
    private var email: TextInputEditText? = null
    private var emailContainer: TextInputLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        email = findViewById<TextInputEditText>(R.id.emailEditText)
        emailContainer = findViewById<TextInputLayout>(R.id.emailContainer)

        email?.let { u ->
            u.doOnTextChanged { text, start, before, count ->
                emailContainer?.let { c -> c.helperText = validateEmail() }
            }
        }
    }

    private fun validateEmail(): String {
        return validator.validateEmail(email?.text.toString().trim())
    }


    fun registration(view: View) {
        val intent = Intent(activity, RegisterPage::class.java)
        startActivity(intent)
        activity.finish()
    }

    fun resetPassword(view: View) {

        if (email?.text.toString().trim() == null) {
            Toast.makeText(activity, "Email invalid", Toast.LENGTH_LONG)
                .show()
        }

        apiRepository.forgotPassword(
            ForgotPasswordRequest(email?.text.toString().trim()),
            object :
                Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (!response.isSuccessful) {
//                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG)
//                            .show()
                        AlertDialog.Builder(activity)
                            .setTitle("Reset password")
                            .setMessage("Something went wrong. Try again later.")
                            .setPositiveButton("Okay") { _, _ ->
                                val intent = Intent(activity, LoginPage::class.java)
                                startActivity(intent)
                                activity.finish()
                            }
                            .show()
                    } else {
                        AlertDialog.Builder(activity)
                            .setTitle("Reset password")
                            .setMessage("Please check your email and confirm resetting password.")
                            .setPositiveButton("Okay") { _, _ ->
                                val intent = Intent(activity, LoginPage::class.java)
                                startActivity(intent)
                                activity.finish()
                            }
                            .show()
                    }

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("Error", t.message.toString())
//                    Toast.makeText(activity, "Server error : " + t.message, Toast.LENGTH_LONG)
//                        .show()
                    AlertDialog.Builder(activity)
                        .setTitle("Reset password")
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