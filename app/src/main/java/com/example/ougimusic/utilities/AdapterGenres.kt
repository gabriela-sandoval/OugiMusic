package com.example.ougimusic.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.ougimusic.CancionesGenero
import com.example.ougimusic.Classes.Genre
import com.example.ougimusic.R

class AdapterGenres(var context: Context, var genres : ArrayList<Genre>) : BaseAdapter() {

    private class ViewHolder(row: View?){
        lateinit var name : TextView
        lateinit var image : ImageView
        lateinit var play : Button

        init {
            this.name = row?.findViewById(R.id.textViewNombreGenero) as TextView
            this.image = row?.findViewById(R.id.generoImage) as ImageView
            this.play = row?.findViewById(R.id.botonPlayGenre) as Button
        }
    }

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view : View?
        var viewHolder : ViewHolder?
        if(convertView == null){
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.generos_list_view, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var genre :Genre = getItem(position) as Genre
        viewHolder.name.text = genre.name
        viewHolder.image.setImageResource(genre.image)


        view?.setOnClickListener{
            var intent = Intent(it.context, CancionesGenero::class.java )
            intent.putExtra("GenreName", genre.name)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            it.context.startActivity(intent)
        }

        return  view as View
    }

    override fun getItem(position: Int): Any {
        return genres.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return genres.count()
    }
}

