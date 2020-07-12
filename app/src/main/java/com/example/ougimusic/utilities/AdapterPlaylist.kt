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

class AdapterPlaylist(var list: List<Playlist>) : Adapter<AdapterPlaylist.PlayListViewHolder>(){

    class PlayListViewHolder(view:View): RecyclerView.ViewHolder(view){
        private val client = OkHttpClient()
        private val global = ContextVariables()

        fun CreateDialog(data: Playlist, contextView: Context): Boolean{
            val builder = AlertDialog.Builder(contextView)
                .setTitle("Borrar lista")
                .setMessage("¿Desea borrar la lista ${data.name}?")
                .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->

                })
                .setPositiveButton("Borrar", DialogInterface.OnClickListener { dialog, which ->
                    val result:Boolean
                    runBlocking (Dispatchers.Default){
                        result = DeletePlaylist(data._id.toString())
                    }
                    if (result){
                        Toast.makeText(itemView.context, "Se borro la lista con exito", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(itemView.context, "No se pudo borrar la lista", Toast.LENGTH_SHORT).show()
                    }
                })
            builder.create().show()
            return true
        }

        fun DeletePlaylist(idList: String) : Boolean{
            val json = """
            {
            "listId": "$idList"
            }
        """.trimIndent()
            var result = false
            val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("${global.rootDirection}playlist/Deletelist")
                .delete(body)
                .build()
            client.newCall(request).execute().use {response ->
                result = response.isSuccessful
            }
            return result
        }

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
            itemView.setOnLongClickListener{
                CreateDialog(data, itemView.context)
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