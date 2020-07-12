package com.example.ougimusic.utilities

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.Canciones
import com.example.ougimusic.CancionesAlbum
import com.example.ougimusic.Classes.Album
import com.example.ougimusic.R

class AdapterAlbum(var list: List<Album>) : RecyclerView.Adapter<AdapterAlbum.AlbumViewHolder>(){

    class AlbumViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bindItem(data: Album){
            val albumName = itemView.findViewById<TextView>(R.id.textViewNombreAlbum)
            albumName.text = data.albumname
            val artist = itemView.findViewById<TextView>(R.id.textViewArtista)
            artist.text = data.artist
            val releaseYear = itemView.findViewById<TextView>(R.id.textViewAño)
            releaseYear.text = data.releaseYear

            itemView.setOnClickListener{
                val intent = Intent(it.context, CancionesAlbum::class.java)
                intent.putExtra("album", data)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val viewSave = LayoutInflater.from(parent.context).inflate(R.layout.album_list_view, parent, false)
        return AlbumViewHolder(viewSave)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindItem(list[position])
    }
}