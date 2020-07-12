package com.example.ougimusic.utilities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AdapterAddToPlaylist(var list: List<Playlist>, var context: Context, var songId:String) : Adapter<AdapterAddToPlaylist.PlayListViewHolder>(){

    class PlayListViewHolder(view:View): RecyclerView.ViewHolder(view){
        private val client = OkHttpClient()
        private val global = ContextVariables()

        fun AddSongToPlaylist(idList: String, idSong: String) : Boolean{
            val json = """
            {
            "listId": "$idList",
            "songs": "[{$idSong}]
            }
        """.trimIndent()
            var result = false
            val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("${global.rootDirection}playlist/AddSong")
                .put(body)
                .build()
            client.newCall(request).execute().use {response ->
                result = response.isSuccessful
            }
            return result
        }

        fun bindItem(data: Playlist, context: Context, songId: String){
            val name = itemView.findViewById<TextView>(R.id.textViewNombrePlaylist)
            name.text = data.name

            itemView.setOnClickListener{
                run{
                    AddSongToPlaylist(data._id.toString(), "$songId");
                }
                (context as Activity).finish()
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
        holder.bindItem(list[position], context, this.songId)
    }

}