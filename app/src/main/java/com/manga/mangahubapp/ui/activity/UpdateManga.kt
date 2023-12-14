package com.manga.mangahubapp.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.enums.Genre
import com.manga.mangahubapp.model.request.UpdateMangaRequest
import com.manga.mangahubapp.model.response.MangaResponse
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
import java.time.format.DateTimeFormatter
import java.util.Calendar

class UpdateManga : AppCompatActivity() {

    private val activity: AppCompatActivity = this@UpdateManga
    private val apiRepository = ApiRepositoryImpl()

    private var token: String? = null
    private var userId: String? = null
    private var mangaId: String? = null

    private var title: TextInputEditText? = null
    private var releasedOn: TextInputEditText? = null
    private var genre: Spinner? = null
    private var description: TextInputEditText? = null

    private var titleContainer: TextInputLayout? = null
    private var releasedOnContainer: TextInputLayout? = null
    private var descriptionContainer: TextInputLayout? = null

    private var datePickerDialog: DatePickerDialog? = null
    private var updateMangaButton: Button? = null
    private var input: TextView? = null

    private var manga: MangaResponse? = null
    private val validator = Validator()
    private var listGenres: HashMap<Int, String> = HashMap<Int, String>()

    private val PICK_IMAGE = 1
    private var avatarUri: String? = null
    private var avatarTemp: ByteArray? = null
    private var avatar: ImageView? = null
    var image: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_manga)
        init()
    }

    private fun init() {
        getExtra()
        token = MainPage.getToken()
        userId = MainPage.getUserId()

        title = findViewById(R.id.titleEditText)
        releasedOn = findViewById(R.id.releasedEditText)
        genre = findViewById(R.id.genreSpinner)
        description = findViewById(R.id.descriptionMangaEditText)
        updateMangaButton = findViewById(R.id.updateMangaButton)

        titleContainer = findViewById(R.id.titleContainer)
        releasedOnContainer = findViewById(R.id.releasedContainer)
        descriptionContainer = findViewById(R.id.descriptionContainer)

        avatar = findViewById<ImageView>(R.id.coverManga)

        avatar.let { a ->
            a!!.setOnClickListener { v ->
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE)
            }
        }

        input = TextView(this)

        updateMangaButton.let { u ->
            u!!.setOnClickListener {
                updateManga()
            }
        }

        val list = Genre.entries.map { g -> g.name }.toMutableList()

        for (g in Genre.entries) {
            listGenres[g.ordinal] = g.name
        }

        list.add("")

        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this, R.layout.spinner_item, list.toList()
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
                    this@UpdateManga.genre!!.setSelection(list.lastIndex)
                }
            }

        genre.let { s ->
            s!!.onItemSelectedListener = itemSelectedListener
        }

        getManga()
    }

    private fun getExtra() {
        val arguments = intent.extras
        if (arguments != null) {
            if (arguments.containsKey("mangaId")) {
                mangaId = arguments.getString("mangaId")
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }
    }

    private fun updateManga() {

        val date = validator.formatDate(releasedOn?.text.toString().trim())
        if (date == null) {
            releasedOnContainer?.helperText = "Invalid date of release"
        }

        val lastUpdateDate = LocalDateTime.now(Clock.systemUTC()).toString()

        if (date != null && lastUpdateDate != null) {

            var updatedManga = UpdateMangaRequest(
                manga!!.mangaId,
                title!!.text.toString(),
                genre!!.selectedItemPosition + 1,
                listGenres,
                description!!.text.toString(),
                date,
                lastUpdateDate,
                image!!,
                userId!!.toInt()
            )

            Log.d("Updated manga", updatedManga.toString())

            apiRepository.updateManga("Bearer " + token, updatedManga, object :
                Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            activity, "Manga was updated successfully!",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(activity, MangaInfo::class.java)
                        intent.putExtra("mangaId", mangaId)
                        startActivity(intent)
                        activity.finish()

                    } else {
                        Log.d("Error1", response.code().toString())

                        Toast.makeText(
                            activity, "Something went wrong during updating!",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(activity, MangaInfo::class.java)
                        intent.putExtra("mangaId", mangaId)
                        startActivity(intent)
                        activity.finish()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("Error2", t.message.toString())

                    Toast.makeText(
                        activity, "Something went wrong during updating!",
                        Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent(activity, MangaInfo::class.java)
                    intent.putExtra("mangaId", mangaId)
                    startActivity(intent)
                    activity.finish()
                }
            })
        } else {
            Log.d("Error3", "date is null")
            Toast.makeText(
                activity, "Something went wrong during updating!",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(activity, MangaInfo::class.java)
            intent.putExtra("mangaId", mangaId)
            startActivity(intent)
            activity.finish()
        }
    }

    private fun getManga() {
        apiRepository.getManga("Bearer " + token, mangaId!!, object :
            Callback<MangaResponse> {
            override fun onResponse(
                call: Call<MangaResponse>,
                response: Response<MangaResponse>
            ) {
                if (response.isSuccessful) {
                    manga = response.body()
                    if (manga != null) {
                        fillData(manga!!)
                    } else {
                        Log.d("Error1", "null manga")
                        Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Log.d("Error2", response.code().toString())
                    Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<MangaResponse>, t: Throwable) {
                Log.d("Error3", t.message.toString())
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun fillData(manga: MangaResponse) {
        var date1 = LocalDateTime.parse(manga.releasedOn)
        var g = Genre.entries[manga.genre.toInt()].name

        image = manga.coverImage

        title!!.setText(manga.title)

        releasedOn!!.setText(date1.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")))

        genre!!.setSelection(manga.genre.toInt() - 1)

        description!!.setText(manga.description)

        val imageBytes = Base64.decode(manga.coverImage, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        avatar!!.setImageBitmap(decodedImage)
    }


    fun onDate(view: View) {
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

                releasedOn?.setText(
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

}