package com.manga.mangahubapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manga.mangahubapp.R
import com.manga.mangahubapp.ui.activity.ChapterInfo

class ChapterAdapter(
    var context: Context?,
    var chapterIds: ArrayList<String>?,
    var titles: ArrayList<String>?
) : RecyclerView.Adapter<ChapterAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d("inside0", "")
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.row_chapter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("inside", chapterIds!![position])
        Log.d("inside13", titles!![position])
        holder.chapterId.setText(chapterIds!![position])
        holder.title.setText("Chapter : " + titles!![position])

        Log.d("inside17", holder.title.text.toString())

        holder.view.setOnClickListener { v ->
            val txt = v.findViewById<TextView>(R.id.chapterId)
            val intent = Intent(context, ChapterInfo::class.java)
            intent.putExtra("chapterId", txt.text.toString())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chapterIds!!.size
    }


    class MyViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chapterId: TextView = itemView.findViewById<TextView>(R.id.chapterId)
        var title: TextView = itemView.findViewById<TextView>(R.id.chapterTitle)
        var view: View = itemView

        fun getItemView(): View {
            return view
        }
    }

}