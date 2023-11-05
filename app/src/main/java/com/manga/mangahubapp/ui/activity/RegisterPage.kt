package com.manga.mangahubapp.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
    private var loginInput: TextInputEditText? = null
    private var passwordInput: TextInputEditText? = null
    private var firstNameInput: TextInputEditText? = null
    private var lastNameInput: TextInputEditText? = null
    private var avatar: ImageView? = null
    private var descriptionInput: TextInputEditText? = null
    private var phoneNumber: TextInputEditText? = null
    private var datePickerDialog: DatePickerDialog? = null
    private var birthDateInput: TextInputEditText? = null
    private var emailInput: TextInputEditText? = null
    private var loginContainer: TextInputLayout? = null
    private var passwordContainer: TextInputLayout? = null
    private var firstNameContainer: TextInputLayout? = null
    private var lastNameContainer: TextInputLayout? = null
    private var descriptionContainer: TextInputLayout? = null
    private var phoneContainer: TextInputLayout? = null
    private var birthDateContainer: TextInputLayout? = null
    private var emailContainer: TextInputLayout? = null
    private val PICK_IMAGE = 1
    var avatarUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {
        loginInput = findViewById<TextInputEditText>(R.id.loginEditText)
        passwordInput = findViewById<TextInputEditText>(R.id.passwordEditText)
        firstNameInput = findViewById<TextInputEditText>(R.id.firstNameEditText)
        lastNameInput = findViewById<TextInputEditText>(R.id.lastNameEditText)
        descriptionInput = findViewById<TextInputEditText>(R.id.descriptionEditText)
        phoneNumber = findViewById<TextInputEditText>(R.id.phoneEditText)
        birthDateInput = findViewById<TextInputEditText>(R.id.dateEditText)
        emailInput = findViewById<TextInputEditText>(R.id.emailEditText)
        avatar = findViewById<ImageView>(R.id.avatar)

        loginContainer = findViewById<TextInputLayout>(R.id.loginContainer)
        passwordContainer = findViewById<TextInputLayout>(R.id.passwordContainer)
        firstNameContainer = findViewById<TextInputLayout>(R.id.firstNameContainer)
        lastNameContainer = findViewById<TextInputLayout>(R.id.lastNameContainer)
        descriptionContainer = findViewById<TextInputLayout>(R.id.descriptionContainer)
        phoneContainer = findViewById<TextInputLayout>(R.id.phoneContainer)
        birthDateContainer = findViewById<TextInputLayout>(R.id.dateContainer)
        emailContainer = findViewById<TextInputLayout>(R.id.emailContainer)
        avatar = findViewById<ImageView>(R.id.avatar)

        avatar.let { a ->
            a!!.setOnClickListener { v ->
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE)
            }
        }

        validate()

    }

    private fun validate() {
        loginInput?.let { u ->
            u.setOnFocusChangeListener { _, _ ->
                loginContainer?.let { c -> c.helperText = validateUsername() }
            }
        }
        passwordInput?.let { u ->
            u.setOnFocusChangeListener { _, _ ->
                passwordContainer?.let { c -> c.helperText = validatePassword() }
            }
        }
        firstNameInput?.let { u ->
            u.setOnFocusChangeListener { _, _ ->
                firstNameContainer?.let { c -> c.helperText = validateFirstName() }
            }
        }
        lastNameContainer?.let { u ->
            u.setOnFocusChangeListener { _, _ ->
                lastNameContainer?.let { c -> c.helperText = validateLastName() }
            }
        }
        descriptionInput?.let { u ->
            u.setOnFocusChangeListener { _, _ ->
                descriptionContainer?.let { c -> c.helperText = validateDescription() }
            }
        }
        phoneNumber?.let { u ->
            u.setOnFocusChangeListener { _, _ ->
                phoneContainer?.let { c -> c.helperText = validatePhone() }
            }
        }
        birthDateInput?.let { u ->
            u.setOnFocusChangeListener { _, _ ->
                birthDateContainer?.let { c -> c.helperText = validateDate() }
            }
        }
        emailInput?.let { u ->
            u.setOnFocusChangeListener { _, _ ->
                emailContainer?.let { c -> c.helperText = validateEmail() }
            }
        }
    }


    private fun validateUsername(): String {
        return validator.validateLogin(loginInput?.text.toString().trim())
    }

    private fun validatePassword(): String {
        return validator.validatePassword(passwordInput?.text.toString().trim())
    }

    private fun validateFirstName(): String {
        return validator.validateFirstName(firstNameInput?.text.toString().trim())
    }

    private fun validateLastName(): String {
        return validator.validateLastName(lastNameInput?.text.toString().trim())
    }

    private fun validateDescription(): String {
        return validator.validateDescription(descriptionInput?.text.toString().trim())
    }

    private fun validatePhone(): String {
        return validator.validatePhone(phoneNumber?.text.toString().trim())
    }

    private fun validateDate(): String {
        return validator.validateDate(birthDateInput?.text.toString().trim())
    }

    private fun validateEmail(): String {
        return validator.validateEmail(emailInput?.text.toString().trim())
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
        val phone = validator.formatPhoneNumber(phoneNumber?.text.toString().trim())
        if (phone == null) {
            phoneContainer?.helperText = "Invalid phone number"
        }

        val date = validator.formatDate(birthDateInput?.text.toString().trim())
        if (date == null) {
            birthDateContainer?.helperText = "Invalid date of birth"
        }

        if (phone != null && date != null) {

            val user = UserRequest(
                loginInput?.text.toString().trim(),
                passwordInput?.text.toString().trim(),
                emailInput?.text.toString().trim(),
                firstNameInput?.text.toString().trim(),
                lastNameInput?.text.toString().trim(),
                descriptionInput?.text.toString().trim(),
                phone, date, "", true
            )

            Log.d("User", user.toString())

            apiRepository.register(user,
                object :
                    Callback<Void> {
                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {
                        if (!response.isSuccessful) {
                            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            val intent = Intent(activity, LoginPage::class.java)
                            startActivity(intent)
                            activity.finish()
                        }

                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("Error", t.message.toString())
                        Toast.makeText(activity, "Server error : " + t.message, Toast.LENGTH_LONG)
                            .show()
                    }
                })
        }
    }
}