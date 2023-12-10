package com.manga.mangahubapp.ui.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.manga.mangahubapp.R


class SearchPage : Fragment() {

    private val activity: Fragment = this@SearchPage
    private var userId: String? = null
    private var names: ArrayList<String>? = null
    private var genres: ArrayList<String>? = null
    private var dates: ArrayList<String>? = null
    private var images: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_search_page, container, false)
        init(v)
        return v
    }

    fun init(v: View) {
        userId = MainPage.getUserId()

        names = null
        genres = null
        dates = null
        images = null

    }

}