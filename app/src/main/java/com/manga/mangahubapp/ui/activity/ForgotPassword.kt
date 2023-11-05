package com.manga.mangahubapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private var email: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        init()
    }

    private fun init() {
        email = findViewById<EditText>(R.id.emailInputField)
    }

    fun registration(view: View) {
        val intent = Intent(activity, RegisterPage::class.java)
        startActivity(intent)
        activity.finish()
    }

    fun resetPassword(view: View) {

        val emailInput = validator.validateEmail(email?.text.toString().trim())

        if (emailInput == null) {
            Toast.makeText(activity, "Email invalid", Toast.LENGTH_LONG)
                .show()
        }

        apiRepository.forgotPassword(
            ForgotPasswordRequest(emailInput!!),
            object :
                Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    Log.d("Tag", response.errorBody().toString())
                    Log.d("Tag", response.headers().toString())
                    Log.d("Tag", response.code().toString())
                    Log.d("Tag", response.isSuccessful.toString())

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("Tag", t.message.toString())
                    Toast.makeText(activity, "Server error : " + t.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
}