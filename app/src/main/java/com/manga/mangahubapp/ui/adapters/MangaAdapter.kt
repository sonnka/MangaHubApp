package com.manga.mangahubapp.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manga.mangahubapp.R


class MangaAdapter(
    var context: Context?,
    var names: ArrayList<String>?,
    var genres: ArrayList<String>?,
    var dates: ArrayList<String>?,
    var images: ArrayList<String>?
) : RecyclerView.Adapter<MangaAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.row_manga, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.date.setText(dates!!.get(position).toString())
        holder.name.setText(names!!.get(position).toString())
        holder.genre.setText(genres!!.get(position).toString())
        holder.image.setImageURI(Uri.parse(images!!.get(position).toString()))
        holder.view.setOnClickListener { v ->
//            val txt = v.findViewById<TextView>(R.id.expense_id)
//            val txt2 = v.findViewById<TextView>(R.id.text)
//            val expenseId = txt.text.toString().toInt()
//            val intent = Intent(context, Info::class.java)
//            intent.putExtra("AutoId", auto_id)
//            intent.putExtra("ExpenseId", expenseId)
//            intent.putExtra("TypeOfExpense", txt2.text.toString())
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
//            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dates!!.size
    }


    class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var date: TextView
        var genre: TextView
        var image: ImageView

        init {
            name = itemView.findViewById<TextView>(R.id.name)
            image = itemView.findViewById<ImageView>(R.id.mangaCover)
            date = itemView.findViewById<TextView>(R.id.date)
            genre = itemView.findViewById<TextView>(R.id.genre)
        }
    }

}