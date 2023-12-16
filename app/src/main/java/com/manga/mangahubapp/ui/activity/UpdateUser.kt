package com.manga.mangahubapp.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.request.UpdateUserRequest
import com.manga.mangahubapp.model.response.UserResponse
import com.manga.mangahubapp.network.ApiRepositoryImpl
import com.manga.mangahubapp.util.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.net.URI
import java.net.URISyntaxException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class UpdateUser : AppCompatActivity() {

    private val activity: AppCompatActivity = this@UpdateUser
    private var token: String? = null
    private var userId: String? = null

    private val apiRepository = ApiRepositoryImpl()
    private var datePickerDialog: DatePickerDialog? = null
    private val validator = Validator()

    private var avatar: ImageView? = null
    private var descriptionInput: TextInputEditText? = null
    private var birthDateInput: TextInputEditText? = null
    private var emailInput: TextInputEditText? = null


    private var descriptionContainer: TextInputLayout? = null
    private var birthDateContainer: TextInputLayout? = null
    private var emailContainer: TextInputLayout? = null

    private val PICK_IMAGE = 1
    private var avatarUri: String? = null
    private var avatarTemp: ByteArray? = null
    var image: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        init()
    }

    private fun init() {
        userId = MainPage.getUserId()
        token = MainPage.getToken()

        descriptionInput = findViewById<TextInputEditText>(R.id.descriptionEditText)
        birthDateInput = findViewById<TextInputEditText>(R.id.dateEditText)
        emailInput = findViewById<TextInputEditText>(R.id.emailEditText)

        descriptionContainer = findViewById<TextInputLayout>(R.id.descriptionContainer)
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

        getUser()
        validate()

    }

    private fun getUser() {

        apiRepository.getUser("Bearer " + token, userId!!.toInt(), object :
            Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        fillData(user)
                    } else {
                        Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(activity, ProfilePage::class.java)
                        startActivity(intent)
                        activity.finish()
                    }
                } else {
                    Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(activity, ProfilePage::class.java)
                    startActivity(intent)
                    activity.finish()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG).show()
                val intent = Intent(activity, ProfilePage::class.java)
                startActivity(intent)
                activity.finish()
            }
        })
    }

    fun fillData(user: UserResponse) {
        var date1 = LocalDateTime.parse(user.birthDate);

        image = user.avatar

        emailInput!!.setText(user.email)
        birthDateInput!!.setText(date1.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")))
        descriptionInput!!.setText(user.description)

        val imageBytes = Base64.decode(user.avatar, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        avatar!!.setImageBitmap(decodedImage)
    }

    private fun validate() {
        descriptionInput?.let { u ->
            u.doOnTextChanged { _, _, _, _ ->
                descriptionContainer?.let { c -> c.helperText = validateDescription() }
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

    private fun validateFirstName(): String {
        return validator.validateFirstName("Testttttt")
    }

    private fun validateLastName(): String {
        return validator.validateLastName("Testttttt")
    }

    private fun validateDescription(): String {
        return validator.validateDescription(descriptionInput?.text.toString().trim())
    }

    private fun validatePhone(): String {
        return validator.validatePhone("+380661936087")
    }

    private fun validateDate(): String {
        return validator.validateDate(birthDateInput?.text.toString().trim())
    }

    private fun validateEmail(): String {
        return validator.validateEmail(emailInput?.text.toString().trim())
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


    fun update(view: View) {
        val phone = validator.formatPhoneNumber("+380661936087")

        val date = validator.formatDate(birthDateInput?.text.toString().trim())
        if (date == null) {
            birthDateContainer?.helperText = "Invalid date of birth"
        }

        if (phone != null && date != null) {

            val user = UpdateUserRequest(
                userId!!.toInt(),
                "Tessssstttt",
                "Testttttttt",
                descriptionInput?.text.toString().trim(),
                "+380661936087",
                true,
                date,
                emailInput?.text.toString().trim(),
                image!!
            )

            Log.d("User", user.toString())

            apiRepository.updateUser("Bearer " + token, user,
                object :
                    Callback<Void> {
                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {
                        if (!response.isSuccessful) {
                            Log.d("Error1", response.code().toString())
                            Toast.makeText(
                                activity,
                                "Something went wrong during updating!",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else {
                            Toast.makeText(activity, "User is updated!", Toast.LENGTH_LONG)
                                .show()
                            val intent = Intent(activity, MainPage::class.java)
                            intent.putExtra("userId", userId)
                            intent.putExtra("token", token)
                            startActivity(intent)
                            activity.finish()
                        }

                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(
                            activity,
                            "Something went wrong during updating!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                })
        }
    }
}