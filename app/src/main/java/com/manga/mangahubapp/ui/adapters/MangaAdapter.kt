package com.manga.mangahubapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
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
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.row_manga, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mangaId.setText(mangaIds!!.get(position).toString())
        holder.rating.setText(ratings!!.get(position).toString())
        holder.name.setText(names!!.get(position).toString())
        holder.genre.setText(genres!!.get(position).toString())
        holder.image.setImageURI(Uri.parse(images!!.get(position).toString()))
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


    class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var mangaId: TextView
        var name: TextView
        var rating: TextView
        var genre: TextView
        var image: ImageView

        init {
            mangaId = itemView.findViewById<TextView>(R.id.mangaId)
            name = itemView.findViewById<TextView>(R.id.name)
            image = itemView.findViewById<ImageView>(R.id.mangaCover)
            rating = itemView.findViewById<TextView>(R.id.rating)
            genre = itemView.findViewById<TextView>(R.id.genre)
        }
    }

}