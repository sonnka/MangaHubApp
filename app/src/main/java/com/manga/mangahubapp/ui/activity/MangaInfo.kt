package com.manga.mangahubapp.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manga.mangahubapp.R
import com.manga.mangahubapp.model.enums.Genre
import com.manga.mangahubapp.model.response.ChapterListItemResponse
import com.manga.mangahubapp.model.response.MangaResponse
import com.manga.mangahubapp.network.ApiRepositoryImpl
import com.manga.mangahubapp.ui.adapters.ChapterAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MangaInfo : AppCompatActivity() {

    private val activity: AppCompatActivity = this@MangaInfo
    private val apiRepository = ApiRepositoryImpl()
    private var token: String? = null
    private var userId: String? = null
    private var mangaId: String? = null
    private var title: TextView? = null
    private var releasedOn: TextView? = null
    private var genre: TextView? = null
    private var description: TextView? = null
    private var editMangaButton: Button? = null
    private var deleteMangaButton: Button? = null
    private var avatar: ImageView? = null

    private var chapterAdapter: ChapterAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var chapterIds: ArrayList<String>? = ArrayList()
    private var titles: ArrayList<String>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_info)
        init()
    }

    private fun init() {
        getExtra()
        token = MainPage.getToken()
        userId = MainPage.getUserId()

        title = findViewById(R.id.titleManga)
        releasedOn = findViewById(R.id.releasedOnManga)
        genre = findViewById(R.id.genreManga)
        description = findViewById(R.id.descriptionManga)
        editMangaButton = findViewById(R.id.editMangaButton)
        deleteMangaButton = findViewById(R.id.deleteMangaButton)

        recyclerView = findViewById(R.id.recycle_view2)

        avatar = findViewById(R.id.coverManga)

        editMangaButton.let { e ->
            e!!.setOnClickListener {
                editManga()
            }
        }

        deleteMangaButton.let { d ->
            d!!.setOnClickListener {
                deleteManga()
            }
        }

        getManga()
    }

    private fun setAdapterOnRecycleView() {
        Log.d("adapter", "")
        chapterAdapter = ChapterAdapter(this, chapterIds, titles)

        recyclerView!!.adapter = chapterAdapter

        recyclerView!!.layoutManager = LinearLayoutManager(this)
    }

    private fun clearRecycleView() {
        chapterIds = ArrayList<String>()
        titles = ArrayList<String>()
    }

    @SuppressLint("Range")
    private fun fillData(dataList: List<ChapterListItemResponse>) {
        if (dataList.size == 0) {
            Toast.makeText(activity, "No chapters!", Toast.LENGTH_LONG).show()
        } else {
            for (item in dataList) {
                Log.d("Response chapter: ", item.title.toString())
                chapterIds!!.add(item.chapterId!!)
                titles!!.add(item.title!!)
            }
        }
    }

    private fun getChapters() {
        apiRepository.getChapters("Bearer " + token, mangaId!!, object :
            Callback<List<ChapterListItemResponse>> {
            override fun onResponse(
                call: Call<List<ChapterListItemResponse>>,
                response: Response<List<ChapterListItemResponse>>
            ) {
                if (response.isSuccessful) {
                    val dataList = response.body()
                    if (dataList != null) {
                        clearRecycleView()
                        setAdapterOnRecycleView()
                        fillData(dataList)
                        setAdapterOnRecycleView()
                    } else {
                        Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<ChapterListItemResponse>>, t: Throwable) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
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
                        getChapters()
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
        var g = Genre.entries[manga.genre.toInt() - 1].name

        title!!.text = manga.title
        releasedOn!!.text =
            "Date of release: " + date1.format(DateTimeFormatter.ofPattern("dd/MM/YYYY"))
        genre!!.text = "Genre: " + g
        description!!.text = "Description: " + manga.description

        val imageBytes = Base64.decode(manga.coverImage, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        avatar!!.setImageBitmap(decodedImage)
    }

    private fun deleteManga() {
        apiRepository.deleteManga("Bearer " + token, mangaId!!, object :
            Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "Manga was deleted!", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(activity, MainPage::class.java)
                    intent.putExtra("userId", userId)
                    intent.putExtra("token", token)
                    startActivity(intent)
                    activity.finish()

                } else {
                    Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun editManga() {
        val intent = Intent(activity, UpdateManga::class.java)
        intent.putExtra("mangaId", mangaId)
        startActivity(intent)
        activity.finish()
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
}