package com.example.ougimusic.utilities

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.ougimusic.Canciones
import com.example.ougimusic.Classes.Playlist
import com.example.ougimusic.R

class AdapterPlaylist(var list: List<Playlist>) : Adapter<AdapterPlaylist.PlayListViewHolder>(){

    class PlayListViewHolder(view:View): RecyclerView.ViewHolder(view){
        fun bindItem(data: Playlist){
            val name = itemView.findViewById<TextView>(R.id.textViewNombrePlaylist)
            name.text = data.name

            itemView.setOnClickListener{
                val intent = Intent(it.context, Canciones::class.java)
                intent.putExtra("playlistName", data.name)
                intent.putExtra("idPlaylist", data.name)
                intent.putStringArrayListExtra("songList", data.songs)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val viewSave = LayoutInflater.from(parent.context).inflate(R.layout.playlist_list_view, parent, false)
        return PlayListViewHolder(viewSave)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bindItem(list[position])
    }
}