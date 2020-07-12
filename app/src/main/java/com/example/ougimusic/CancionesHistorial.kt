package com.example.ougimusic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
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
        val title = findViewById<TextView>(R.id.textViewPlaylistName)
        title.text = "Historial de reproducción"

        val recycler: RecyclerView = findViewById<RecyclerView>(R.id.recyclerPlaylistSongs)
        recycler.layoutManager = LinearLayoutManager(parent, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter
        var queue = (this.application as ContextVariables).getQueue()

        ReadData(queue)



        buttonRegresar.setOnClickListener {
            finish()
        }

    }

    fun playAll(v: View){
        if(songDataList.any()){
            var queue = Queue()
            queue.currentList = songDataList
            queue.currentSongPosition = 0
            val intent = Intent(this, InicioReproductor::class.java)
            intent.putExtra("Current_List", queue)
            startActivity(intent)
        }
    }

    fun ReadData(queue: Queue?) {
        for (song in queue?.currentList!!){
            songDataList.add(song)
        }
        adapter.notifyDataSetChanged()
    }


}