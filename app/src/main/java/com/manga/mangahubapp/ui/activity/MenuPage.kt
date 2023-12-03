package com.manga.mangahubapp.ui.activity
//
//import android.annotation.SuppressLint
//import android.database.Cursor
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.SearchView
//import android.widget.Toast
import androidx.fragment.app.Fragment

//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.manga.mangahubapp.R
//import com.manga.mangahubapp.ui.adapters.MangaAdapter
//
//
class MenuPage : Fragment() {
//
//    private val activity: Fragment = this@MenuPage
//    var searchView: SearchView? = null
//    var recyclerView: RecyclerView? = null
//    var mangaAdapter: MangaAdapter? = null
//    var names: ArrayList<String>? = null;
//    var genres: ArrayList<String>? = null;
//    var dates: ArrayList<String>? = null;
//    var images: ArrayList<String>? = null;
//
//
//    fun ListOfExpenses() {}
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val v: View = inflater.inflate(R.layout.fragment_menu_page, container, false)
//        init(v)
//        return v
//    }
//
//    fun init(v: View) {
//
//        names = ArrayList()
//        dates = ArrayList()
//        genres = ArrayList()
//        images = ArrayList()
//
//        recyclerView = v.findViewById<RecyclerView>(R.id.recycle_view)
//        searchView = v.findViewById(R.id.search)
//
//        searchView?.let { sv ->
//            sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String): Boolean {
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String): Boolean {
//                    filterDate(newText)
//                    return false
//                }
//            })
//            sv.setOnCloseListener {
//                clearRecycleView()
//                displayAllData()
//                setAdapterOnRecycleView()
//                false
//            }
//        }
//
//
//        displayAllData()
//        setAdapterOnRecycleView()
//    }
//
//    private fun filterDate(searchText: String) {
//        val cursor: Cursor = dbHelper.searchByDate(auto_id, searchText)
//        if (cursor.count == 0) {
//            val cursor2: Cursor = dbHelper.searchByDescription(auto_id, searchText)
//            clearRecycleView()
//            fillData(cursor2)
//            setAdapterOnRecycleView()
//        } else {
//            clearRecycleView()
//            fillData(cursor)
//            setAdapterOnRecycleView()
//        }
//    }
//
//    private fun clearRecycleView() {
//        names = ArrayList()
//        dates = ArrayList()
//        genres = ArrayList()
//        images = ArrayList()
//    }
//
//    private fun setAdapterOnRecycleView() {
//        mangaAdapter = MangaAdapter(
//            activity.context, names,
//            genres, dates, images
//        )
//        recyclerView!!.adapter = mangaAdapter
//        recyclerView!!.layoutManager = LinearLayoutManager(activity.context)
//    }
//
//    @SuppressLint("Range")
//    private fun displayAllData() {
//        val cursor: Cursor = dbHelper.readAllDataByAutoId(auto_id)
//        fillData(cursor)
//    }
//
//    private fun fillData(cursor: Cursor) {
//        if (cursor.count == 0) {
//            Toast.makeText(activity.context, "No data!", Toast.LENGTH_LONG).show()
//        } else {
//            while (cursor.moveToNext()) {
//                names!!.add(cursor.getString(0))
//                genres!!.add(cursor.getString(2))
//                dates!!.add(cursor.getString(3))
//                images!!.add(cursor.getString(4))
//            }
//        }
//    }
}