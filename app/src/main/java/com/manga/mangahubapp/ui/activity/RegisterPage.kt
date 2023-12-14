package com.manga.mangahubapp.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.request.UserRequest
import com.manga.mangahubapp.network.ApiRepositoryImpl
import com.manga.mangahubapp.util.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
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
    var image: String? = ""

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
            u.doOnTextChanged { _, _, _, _ ->
                loginContainer?.let { c -> c.helperText = validateUsername() }
            }
        }
        passwordInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                passwordContainer?.let { c -> c.helperText = validatePassword() }
            }
        }
        firstNameInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                firstNameContainer?.let { c -> c.helperText = validateFirstName() }
            }
        }
        lastNameInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                lastNameContainer?.let { c -> c.helperText = validateLastName() }
            }
        }
        descriptionInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                descriptionContainer?.let { c -> c.helperText = validateDescription() }
            }
        }
        phoneNumber?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                phoneContainer?.let { c -> c.helperText = validatePhone() }
            }
        }
        birthDateInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                birthDateContainer?.let { c -> c.helperText = validateDate() }
            }
        }
        emailInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
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

                var month = (monthOfYear + 1).toString()
                if (month.length < 2) {
                    month = "0$month";
                }

                birthDateInput?.setText(
                    "$day/$month/$year"
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


            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)

            image = convertBitmapToBase64(bitmap)

            avatarUri = uri.toString()
            activity.contentResolver
                .takePersistableUriPermission(
                    selectedImageUri!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream) // Adjust quality as needed
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }


//    private fun getAvatar(avatar: ImageView?): String? {
//        val drawable = avatar!!.drawable
//        if (drawable != null) {
//            val bitmap = (drawable).toBitmap()
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//            val byteArray = byteArrayOutputStream.toByteArray()
//            return Base64.encodeToString(byteArray, Base64.DEFAULT)
//        }
//        return null
//    }


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
                phone, date, image!!, true
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
                            Log.d("Error1", response.code().toString())
                            AlertDialog.Builder(activity)
                                .setTitle("Sing up")
                                .setMessage("Something went wrong. Try again later.")
                                .setPositiveButton("Okay") { _, _ ->
                                    val intent = Intent(activity, LoginPage::class.java)
                                    startActivity(intent)
                                    activity.finish()
                                }
                                .show()
                        } else {
                            AlertDialog.Builder(activity)
                                .setTitle("Sign up")
                                .setMessage("Registration was successful. You can try to log into your account.")
                                .setPositiveButton("Okay") { _, _ ->
                                    val intent = Intent(activity, LoginPage::class.java)
                                    startActivity(intent)
                                    activity.finish()
                                }
                                .show()
                        }

                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("Error2", t.message.toString())
                        AlertDialog.Builder(activity)
                            .setTitle("Sing up")
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
}