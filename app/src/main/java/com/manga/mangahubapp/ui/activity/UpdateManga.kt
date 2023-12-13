package com.manga.mangahubapp.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.enums.Genre
import com.manga.mangahubapp.model.response.MangaResponse
import com.manga.mangahubapp.network.ApiRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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



        input = TextView(this)

        updateMangaButton.let { u ->
            u!!.setOnClickListener {
                updateManga()
            }
        }

        val list = Genre.entries.map { g -> g.name }.toMutableList()

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
        TODO("Not yet implemented")
    }

    private fun getManga() {
        apiRepository.getManga("Bearer " + token, mangaId!!, object :
            Callback<MangaResponse> {
            override fun onResponse(
                call: Call<MangaResponse>,
                response: Response<MangaResponse>
            ) {
                if (response.isSuccessful) {
                    val manga = response.body()
                    if (manga != null) {
                        fillData(manga)
                    } else {
                        Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<MangaResponse>, t: Throwable) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun fillData(manga: MangaResponse) {
        var date1 = LocalDateTime.parse(manga.releasedOn)
        var g = Genre.entries[manga.genre.toInt()].name

        title!!.setText(manga.title)

        releasedOn!!.setText(date1.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")))

        genre!!.setSelection(manga.genre.toInt())

        description!!.setText(manga.description)
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
}