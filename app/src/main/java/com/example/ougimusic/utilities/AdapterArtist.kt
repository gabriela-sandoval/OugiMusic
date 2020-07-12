package com.example.ougimusic.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.AlbumesArtistas
import com.example.ougimusic.CancionesGenero
import com.example.ougimusic.Classes.Artist
import com.example.ougimusic.Classes.Genre
import com.example.ougimusic.Classes.Queue
import com.example.ougimusic.Classes.Song
import com.example.ougimusic.InicioReproductor
import com.example.ougimusic.R
import com.squareup.picasso.Picasso

class AdapterArtist (var list: List<Artist>) : RecyclerView.Adapter<AdapterArtist.ArtistViewHolder>() {


    class ArtistViewHolder(view:View): RecyclerView.ViewHolder(view){

        fun bindItem(data: Artist){
            val name = itemView.findViewById<TextView>(R.id.textViewNombreGenero)
            name.text = data.artist
            val description = itemView.findViewById<TextView>(R.id.textViewArtista)
            description.text = data.description
            val genero = itemView.findViewById<TextView>(R.id.textView4)
            genero.text = data.genre
            val año = itemView.findViewById<TextView>(R.id.textViewAño)
            año.text = data.debutYear
            val songImage = itemView.findViewById<ImageView>(R.id.generoImage)
            Picasso.get().load(data?.urlImage).into(songImage);


            itemView.setOnClickListener{
                val intent = Intent(it.context, AlbumesArtistas::class.java)
                intent.putExtra("Current_Artist", data.artist)
                it.context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val viewSave = LayoutInflater.from(parent.context).inflate(R.layout.artista_list_view, parent, false)
        return ArtistViewHolder(viewSave)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bindItem(list[position])
    }
}
