package com.manga.mangahubapp.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
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
        releasedOn = v.findViewById(R.id.releasedEditText)
        genre = v.findViewById(R.id.genreSpinner)
        description = v.findViewById(R.id.descriptionMangaEditText)
        createMangaButton = v.findViewById(R.id.createMangaButton)

        titleContainer = v.findViewById(R.id.titleContainer)
        releasedOnContainer = v.findViewById(R.id.releasedContainer)
        descriptionContainer = v.findViewById(R.id.descriptionContainer)

        input = TextView(this.requireContext())

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
                genre!!.selectedItemPosition,
                description!!.text.toString(),
                date,
                creationDate,
                "",
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
                            activity.requireContext(), "Manga was updated successfully!",
                            Toast.LENGTH_LONG
                        ).show()

                        val intent = Intent(activity.requireContext(), SearchPage::class.java)
                        startActivity(intent)

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


    fun onDate(view: View) {
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
}