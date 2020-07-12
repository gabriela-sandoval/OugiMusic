package com.example.ougimusic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.Classes.Queue
import com.example.ougimusic.Classes.Song
import com.example.ougimusic.utilities.AdapterSongsList
import com.example.ougimusic.utilities.ContextVariables
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import okhttp3.OkHttpClient

class CancionesHistorial : AppCompatActivity() {


    val songDataList = mutableListOf<Song>()
    val adapter = AdapterSongsList(songDataList)
    val global = ContextVariables()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canciones)

        val recycler: RecyclerView = findViewById<RecyclerView>(R.id.recyclerPlaylistSongs)
        recycler.layoutManager = LinearLayoutManager(parent, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter
        var queue = (this.application as ContextVariables).getQueue()

        Log.d("canciones" , queue?.currentList?.get(0)?.title)
        ReadData(queue)



        buttonRegresar.setOnClickListener {
            finish()
        }

    }

    fun ReadData(queue: Queue?) {
        for (song in queue?.currentList!!){
            songDataList.add(song)
        }
        adapter.notifyDataSetChanged()
    }


}