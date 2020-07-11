package com.example.ougimusic.utilities


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.ougimusic.Canciones
import com.example.ougimusic.Classes.Song
import com.example.ougimusic.R

class AdapterSongsList(var list: List<Song>) : Adapter<AdapterSongsList.SongsViewHolder>(){


    class SongsViewHolder(view:View): RecyclerView.ViewHolder(view){
        fun bindItem(data: Song){
            val title = itemView.findViewById<TextView>(R.id.textViewNombreCancion)
            title.text = data.title
            val artist = itemView.findViewById<TextView>(R.id.textViewArtista)
            artist.text = data.artist
            val album = itemView.findViewById<TextView>(R.id.textViewAlbum)
            album.text = data.album
            val año = itemView.findViewById<TextView>(R.id.textViewAño)
            año.text = data.year


            itemView.setOnClickListener{
                Toast.makeText(it.context, "${data.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val viewSave = LayoutInflater.from(parent.context).inflate(R.layout.cancion_list_view, parent, false)
        return SongsViewHolder(viewSave)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bindItem(list[position])
    }
}