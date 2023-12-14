package com.manga.mangahubapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manga.mangahubapp.R
import com.manga.mangahubapp.ui.activity.MangaInfo


class MangaAdapter(
    var context: Context?,
    var mangaIds: ArrayList<String>?,
    var names: ArrayList<String>?,
    var genres: ArrayList<String>?,
    var ratings: ArrayList<Double>?,
    var images: ArrayList<String>?
) : RecyclerView.Adapter<MangaAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d("inside0", "")
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.row_manga, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("inside", mangaIds!![position].toString())
        holder.mangaId.setText(mangaIds!![position].toString())
        holder.rating.setText(ratings!![position].toString() + "/5")
        holder.name.setText(names!![position].toString())
        holder.genre.setText(genres!![position].toString())
        //  holder.image.setImageURI(Uri.parse(images!!.get(position).toString()))
        holder.view.setOnClickListener { v ->
            val txt = v.findViewById<TextView>(R.id.mangaId)
            val intent = Intent(context, MangaInfo::class.java)
            intent.putExtra("mangaId", txt.text.toString())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return names!!.size
    }


    class MyViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mangaId: TextView = itemView.findViewById<TextView>(R.id.mangaId)
        var name: TextView = itemView.findViewById<TextView>(R.id.name)
        var rating: TextView = itemView.findViewById<TextView>(R.id.rating)
        var genre: TextView = itemView.findViewById<TextView>(R.id.genreOutput)
        var image: ImageView = itemView.findViewById<ImageView>(R.id.mangaCover)
        var view: View = itemView

        fun getItemView(): View {
            return view
        }
    }

}