package com.manga.mangahubapp.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.manga.mangahubapp.R
import com.manga.mangahubapp.network.ApiRepositoryImpl
import java.net.URI
import java.net.URISyntaxException
import java.util.Calendar


class RegisterPage : AppCompatActivity() {

    private val activity: AppCompatActivity = this@RegisterPage
    private val apiRepository = ApiRepositoryImpl()
    private var login: EditText? = null
    private var password: EditText? = null
    private var firstName: EditText? = null
    private var lastName: EditText? = null
    private var avatar: ImageView? = null
    private var description: EditText? = null
    private var phoneNumber: EditText? = null
    private var datePickerDialog: DatePickerDialog? = null
    private var birthDate: EditText? = null
    private var email: EditText? = null
    private val PICK_IMAGE = 1
    var avatarUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {
        login = findViewById<EditText>(R.id.loginInput)
        password = findViewById<EditText>(R.id.passwordInput)
        firstName = findViewById<EditText>(R.id.firstNameInput)
        lastName = findViewById<EditText>(R.id.lastnameInput)
        description = findViewById<EditText>(R.id.descriptionInput)
        phoneNumber = findViewById<EditText>(R.id.phoneNumberInput)
        birthDate = findViewById<EditText>(R.id.dateInput)
        email = findViewById<EditText>(R.id.emailInput)
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
                birthDate?.setText(
                    dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
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


}