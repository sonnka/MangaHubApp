package com.manga.mangahubapp.ui.activity


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.enums.Genre
import com.manga.mangahubapp.model.request.SearchRequest
import com.manga.mangahubapp.model.response.MangaListItemResponse
import com.manga.mangahubapp.network.ApiRepositoryImpl
import com.manga.mangahubapp.ui.adapters.MangaAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchPage() : Fragment() {

    private val activity: Fragment = this@SearchPage
    private var userId: String? = null
    private var mangaIds: ArrayList<String>? = null
    private var names: ArrayList<String>? = null
    private var genres: ArrayList<String>? = null
    private var ratings: ArrayList<Double>? = null
    private var images: ArrayList<String>? = null
    private var recyclerView: RecyclerView? = null
    private var searchField: EditText? = null
    private var searchButton: ImageButton? = null
    private var resetButton: ImageButton? = null
    private var rating: EditText? = null
    private var spinner: Spinner? = null
    private var mangaAdapter: MangaAdapter? = null
    private val apiRepository = ApiRepositoryImpl()
    private var genre: TextView? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilePage().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_search_page, container, false)
        init(v)
        return v
    }

    fun init(v: View) {
        userId = MainPage.getUserId()
        token = MainPage.getToken()

        mangaIds = ArrayList<String>()
        names = ArrayList<String>()
        genres = ArrayList<String>()
        ratings = ArrayList<Double>()
        images = ArrayList<String>()

        genre = TextView(this.requireContext())

        recyclerView = v.findViewById(R.id.recycle_view)
        searchField = v.findViewById(R.id.searchField)
        searchButton = v.findViewById(R.id.searchButton)
        resetButton = v.findViewById(R.id.resetButton)
        rating = v.findViewById(R.id.rating)
        spinner = v.findViewById(R.id.spinner)

        searchButton.let { s ->
            s!!.setOnClickListener(View.OnClickListener {
                clearRecycleView()
                displayAllData()
            })
        }

        resetButton.let { s ->
            s!!.setOnClickListener(View.OnClickListener {
                searchField.let { s ->
                    s!!.setText("")
                }
                rating.let { r ->
                    r!!.setText("")
                }

                clearRecycleView()
                displayAllData()
            })
        }

        val list = Genre.entries.map { g -> g.name }.toMutableList()

        list.add("")

        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this.requireContext(), R.layout.spinner_item, list.toList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        spinner.let { s ->
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
                    genre!!.setText(item)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    genre!!.setText("Select genre")
                    this@SearchPage.spinner!!.setSelection(list.lastIndex)
                }
            }

        spinner.let { s ->
            s!!.onItemSelectedListener = itemSelectedListener
        }

        displayAllData()
    }

    private fun setAdapterOnRecycleView() {
        Log.d("adapter", "")
        mangaAdapter = MangaAdapter(
            getActivity(), mangaIds, names, genres, ratings, images
        )

        recyclerView!!.adapter = mangaAdapter

        recyclerView!!.layoutManager = LinearLayoutManager(getActivity())
    }

    private fun displayAllData() {

        var search: SearchRequest? = null
        if (rating!!.text.isEmpty() || genre!!.text.isEmpty() || searchField!!.text.isEmpty()) {
            Log.d("v1", "dddddd")
            search = SearchRequest(
                "",
                "",
                null,
                0.0
            )
        } else {
            Log.d("v2", "dddddd")
            search = SearchRequest(
                searchField!!.text.toString(),
                genre!!.text.toString(),
                null,
                rating!!.text.toString().toDouble()
            )
        }
        Log.d("Request ", token + " \n " + search.toString())
        apiRepository.getMangas("Bearer " + token, search, object :
            Callback<List<MangaListItemResponse>> {
            override fun onResponse(
                call: Call<List<MangaListItemResponse>>,
                response: Response<List<MangaListItemResponse>>
            ) {
                if (response.isSuccessful) {
                    val dataList = response.body()
                    if (dataList != null) {
                        fillData(dataList)
                        Log.d("List ", dataList.toString())

                    } else {
                        Toast.makeText(getActivity(), "Something went wrong1!", Toast.LENGTH_LONG)
                            .show()
                    }
                    setAdapterOnRecycleView()
                } else {
                    Log.d("Error2", response.code().toString())
                    Toast.makeText(getActivity(), "Something went wrong!2", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<MangaListItemResponse>>, t: Throwable) {
                Log.d("error3", t.message.toString())
                Toast.makeText(getActivity(), "Something went wrong!3", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun clearRecycleView() {
        mangaIds = ArrayList<String>()
        names = ArrayList<String>()
        genres = ArrayList<String>()
        ratings = ArrayList<Double>()
        images = ArrayList<String>()
    }

    @SuppressLint("Range")
    private fun fillData(dataList: List<MangaListItemResponse>) {
        if (dataList.size == 0) {
            Toast.makeText(activity.context, "No data!", Toast.LENGTH_LONG).show()
        } else {
            for (item in dataList) {
                mangaIds!!.add(item.mangaId)
                names!!.add(item.title)
                genres!!.add(Genre.entries[item.genre.toInt()].name)
                ratings!!.add(item.rating)
                //images!!.add(item.coverImage.toString())
                images!!.add("")
            }
        }
    }
}