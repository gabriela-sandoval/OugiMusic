package com.example.ougimusic.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.ougimusic.R

class AdapterPlaylist(var list: AbstractList<PlaylistData.Playlist>) : Adapter<AdapterPlaylist.PlayListViewHolder>(){


    class PlayListViewHolder(view:View): RecyclerView.ViewHolder(view){
        fun bindItem(data: PlaylistData.Playlist){
            val name = itemView.findViewById<TextView>(R.id.textViewNombrePlaylist)
            name.text = data.name

            itemView.setOnClickListener{
                Toast.makeText(itemView.context, "Id de este: ${data._id}", Toast.LENGTH_SHORT).show()
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