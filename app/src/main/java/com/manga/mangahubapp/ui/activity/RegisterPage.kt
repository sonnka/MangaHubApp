package com.manga.mangahubapp.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.UserRequest
import com.manga.mangahubapp.network.ApiRepositoryImpl
import com.manga.mangahubapp.util.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI
import java.net.URISyntaxException
import java.util.Calendar


class RegisterPage : AppCompatActivity() {

    private val activity: AppCompatActivity = this@RegisterPage
    private val apiRepository = ApiRepositoryImpl()
    private val validator = Validator()
    private var loginInput: EditText? = null
    private var passwordInput: EditText? = null
    private var firstNameInput: EditText? = null
    private var lastNameInput: EditText? = null
    private var avatar: ImageView? = null
    private var descriptionInput: EditText? = null
    private var phoneNumber: EditText? = null
    private var datePickerDialog: DatePickerDialog? = null
    private var birthDateInput: EditText? = null
    private var emailInput: EditText? = null
    private var login: String? = null
    private var password: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var description: String? = null
    private var phone: String? = null
    private var birthDate: String? = null
    private var email: String? = null
    private val PICK_IMAGE = 1
    var avatarUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {
        loginInput = findViewById<EditText>(R.id.usernameInput)
        passwordInput = findViewById<EditText>(R.id.passwordInput)
        firstNameInput = findViewById<EditText>(R.id.firstNameInput)
        lastNameInput = findViewById<EditText>(R.id.lastnameInput)
        descriptionInput = findViewById<EditText>(R.id.descriptionInput)
        phoneNumber = findViewById<EditText>(R.id.phoneNumberInput)
        birthDateInput = findViewById<EditText>(R.id.dateInput)
        emailInput = findViewById<EditText>(R.id.emailInput)
        avatar = findViewById<ImageView>(R.id.avatar)

        avatar.let { a ->
            a!!.setOnClickListener { v ->
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE)
            }
        }

    }


    @SuppressLint("SetTextI18n")
    fun onDate(view: View?) {
        val calendar: Calendar = Calendar.getInstance()
        val mYear: Int = calendar.get(Calendar.YEAR)
        val mMonth: Int = calendar.get(Calendar.MONTH)
        val mDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(
            activity,
            { view1: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                var day = dayOfMonth.toString()
                if (day.length < 2) {
                    day = "0$day";
                }
                birthDateInput?.setText(
                    day + "/" + (monthOfYear + 1) + "/" + year
                )
            }, mYear, mMonth, mDay
        )
        datePickerDialog!!.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            val selectedImageUri = data?.data
            var uri: URI? = null
            try {
                uri = URI(selectedImageUri.toString())
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            avatar!!.setImageURI(selectedImageUri)
            avatarUri = uri.toString()
            activity.contentResolver
                .takePersistableUriPermission(
                    selectedImageUri!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
        }
    }


    fun register(view: View) {


        validate()


        val user = UserRequest(
            login!!, password!!, email!!, firstName!!, lastName!!,
            description!!, phone!!, birthDate!!, "", true
        )

        Log.d("User", user.toString())

        apiRepository.register(user,
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

    private fun validate() {
        validateLogin()
        validateEmail()
        validatePassword()
        validatePhone()
        validateDate()

        firstName = firstNameInput?.text.toString().trim()
        if (firstName == null) {
            Toast.makeText(activity, "First name validation error", Toast.LENGTH_LONG)
                .show()
        }

        lastName = lastNameInput?.text.toString().trim()
        if (lastName == null) {
            Toast.makeText(activity, "First name validation error", Toast.LENGTH_LONG)
                .show()
        }

        description = descriptionInput?.text.toString().trim()
        if (description == null) {
            Toast.makeText(activity, "Description validation error", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validateLogin() {
        login = validator.validateLogin(loginInput?.text.toString().trim())
        if (login == null) {
            Toast.makeText(activity, "Login validation error", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validatePassword() {
        password = validator.validatePassword(passwordInput?.text.toString().trim())
        if (password == null) {
            Toast.makeText(activity, "Password validation error", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validateEmail() {
        email = validator.validateEmail(emailInput?.text.toString().trim())
        if (email == null) {
            Toast.makeText(activity, "Email validation error", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validatePhone() {
        phone = validator.validatePhoneNumber(phoneNumber?.text.toString().trim());
        if (phone == null) {
            Toast.makeText(activity, "Phone validation error", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun validateDate() {
        birthDate = validator.validateDate(birthDateInput?.text.toString().trim());
        if (birthDate == null) {
            Toast.makeText(activity, "Date validation error", Toast.LENGTH_LONG)
                .show()
        }
    }

}