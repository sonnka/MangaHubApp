package com.manga.mangahubapp.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.enums.Genre
import com.manga.mangahubapp.model.request.MangaRequest
import com.manga.mangahubapp.network.ApiRepositoryImpl
import com.manga.mangahubapp.util.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.net.URI
import java.net.URISyntaxException
import java.time.Clock
import java.time.LocalDateTime
import java.util.Calendar

class CreateMangaPage : Fragment() {

    private val activity: Fragment = this@CreateMangaPage
    private val apiRepository = ApiRepositoryImpl()

    private var token: String? = null
    private var userId: String? = null

    private var title: TextInputEditText? = null
    private var releasedOn: TextInputEditText? = null
    private var genre: Spinner? = null
    private var description: TextInputEditText? = null

    private var titleContainer: TextInputLayout? = null
    private var releasedOnContainer: TextInputLayout? = null
    private var descriptionContainer: TextInputLayout? = null

    private var datePickerDialog: DatePickerDialog? = null
    private var createMangaButton: Button? = null
    private var input: TextView? = null

    private val validator = Validator()
    private var list: List<String> = ArrayList<String>()
    private var listGenres: HashMap<Int, String> = HashMap<Int, String>()

    private val PICK_IMAGE = 1
    private var avatarUri: String? = null
    private var avatarTemp: ByteArray? = null
    private var avatar: ImageView? = null
    var image: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_create_manga_page, container, false)
        init(v)
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateMangaPage().apply {
                arguments = Bundle().apply {
                }
            }
    }


    private fun init(v: View) {
        token = MainPage.getToken()
        userId = MainPage.getUserId()

        title = v.findViewById(R.id.titleEditText)
        releasedOn = v.findViewById(R.id.releaseEditText)
        genre = v.findViewById(R.id.genreSpinner)
        description = v.findViewById(R.id.descriptionMangaEditText)
        createMangaButton = v.findViewById(R.id.createMangaButton)

        titleContainer = v.findViewById(R.id.titleContainer)
        releasedOnContainer = v.findViewById(R.id.releasedContainer)
        descriptionContainer = v.findViewById(R.id.descriptionContainer)

        avatar = v.findViewById<ImageView>(R.id.coverManga)

        avatar.let { a ->
            a!!.setOnClickListener { v ->
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE)
            }
        }

        input = TextView(this.requireContext())

        for (g in Genre.entries) {
            listGenres[g.ordinal] = g.name
        }

        releasedOn.let { r ->
            r!!.setOnClickListener {
                onGetDate(r.rootView)
            }
        }

        createMangaButton.let { u ->
            u!!.setOnClickListener {
                createManga()
            }
        }

        val tempList = Genre.entries.map { g -> g.name }.toMutableList()

        tempList.add("")

        list = tempList.toList()

        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this.requireContext(), R.layout.spinner_item, list
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        genre.let { s ->
            s!!.adapter = adapter
        }

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val item = parent.getItemAtPosition(position) as String
                    input!!.setText(item)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    input!!.setText("Select genre")
                    genre!!.setSelection(list.lastIndex)
                }
            }

        genre.let { s ->
            s!!.onItemSelectedListener = itemSelectedListener
        }

        genre!!.setSelection(list.lastIndex)

    }

    private fun createManga() {

        val date = validator.formatDate(releasedOn?.text.toString().trim())
        if (date == null) {
            releasedOnContainer?.helperText = "Invalid date of release"
        }

        val creationDate = LocalDateTime.now(Clock.systemUTC()).toString()

        if (date != null && creationDate != null) {

            var createManga = MangaRequest(
                title!!.text.toString(),
                genre!!.selectedItemPosition + 1,
                listGenres,
                description!!.text.toString(),
                date,
                creationDate,
                image!!,
                userId!!.toInt()
            )

            Log.d("Created manga", createManga.toString())

            apiRepository.createManga("Bearer " + token, createManga, object :
                Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            activity.requireContext(), "Manga was created successfully!",
                            Toast.LENGTH_LONG
                        ).show()

                        cleanFields()

                    } else {
                        Toast.makeText(
                            activity.requireContext(), "Something went wrong during creating!",
                            Toast.LENGTH_LONG
                        ).show()

                        cleanFields()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        activity.requireContext(), "Something went wrong during creating!",
                        Toast.LENGTH_LONG
                    ).show()

                    cleanFields()
                }
            })
        } else {
            Toast.makeText(
                activity.requireContext(), "Something went wrong during updating!",
                Toast.LENGTH_LONG
            ).show()

            cleanFields()
        }
    }

    private fun cleanFields() {
        title!!.setText("")
        releasedOn!!.setText("")
        description!!.setText("")

        input!!.setText("Select genre")
        genre!!.setSelection(list.lastIndex)
    }

    @SuppressLint("SetTextI18n")
    fun onGetDate(view: View?) {
        val calendar: Calendar = Calendar.getInstance()
        val mYear: Int = calendar.get(Calendar.YEAR)
        val mMonth: Int = calendar.get(Calendar.MONTH)
        val mDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(
            activity.requireContext(),
            { view1: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                var day = dayOfMonth.toString()
                if (day.length < 2) {
                    day = "0$day";
                }

                var month = (monthOfYear + 1).toString()
                if (month.length < 2) {
                    month = "0$month";
                }

                releasedOn?.setText(
                    "$day/$month/$year"
                )
            }, mYear, mMonth, mDay
        )
        datePickerDialog!!.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == PICK_IMAGE) {
            val selectedImageUri = data?.data
            var uri: URI? = null
            try {
                uri = URI(selectedImageUri.toString())
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            avatar!!.setImageURI(selectedImageUri)


            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(
                    this.requireContext().contentResolver,
                    selectedImageUri
                )

            image = convertBitmapToBase64(bitmap)

            avatarUri = uri.toString()
            this.requireContext().contentResolver
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

}