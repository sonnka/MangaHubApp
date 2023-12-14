package com.manga.mangahubapp.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.response.UserResponse
import com.manga.mangahubapp.network.ApiRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ProfilePage : Fragment() {

    private val activity: Fragment = this@ProfilePage
    private var token: String? = null
    private var userId: String? = null
    private var firstName: TextView? = null
    private var lastName: TextView? = null
    private var email: TextView? = null
    private var date: TextView? = null
    private var phone: TextView? = null
    private var description: TextView? = null
    private val apiRepository = ApiRepositoryImpl()
    private var editUserButton: MaterialButton? = null
    private var signout: TextView? = null
    private var avatar: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_profile_page, container, false)
        init(v)
        return v
    }

    private fun init(v: View) {
        userId = MainPage.getUserId()
        token = MainPage.getToken()

        firstName = v.findViewById(R.id.firstNameText)
        lastName = v.findViewById(R.id.lastNameText)
        email = v.findViewById(R.id.emailText)
        date = v.findViewById(R.id.dateOfBirthText)
        phone = v.findViewById(R.id.phoneText)
        description = v.findViewById(R.id.descriptionText)

        editUserButton = v.findViewById(R.id.editUserButton)
        signout = v.findViewById(R.id.singOut)

        avatar = v.findViewById(R.id.avatar)

        editUserButton.let { e ->
            e!!.setOnClickListener {
                editUser()
            }
        }

        signout.let { s ->
            s!!.setOnClickListener {
                logout()
            }
        }

        getUser()
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
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun fillData(user: UserResponse) {
        var date1 = LocalDateTime.parse(user.birthDate);

        firstName!!.text = user.firstName
        lastName!!.text = user.lastName
        email!!.text = "Email: " + user.email
        date!!.text = "Date of birth: " + date1.format(DateTimeFormatter.ofPattern("dd/MM/YYYY"))
        phone!!.text = "Phone: " + user.phoneNumber
        description!!.text = "Description: " + user.description

        val imageBytes = Base64.decode(user.avatar, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        avatar!!.setImageBitmap(decodedImage)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilePage().apply {
                arguments = Bundle().apply {

                }
            }
    }

    fun editUser() {
        val intent = Intent(activity.context, UpdateUser::class.java)
        startActivity(intent)
    }

    fun logout() {
        val intent = Intent(activity.context, LoginPage::class.java)
        startActivity(intent)
        activity.requireParentFragment().requireActivity().finish()
    }
}