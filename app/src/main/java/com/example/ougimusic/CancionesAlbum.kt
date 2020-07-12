package com.example.ougimusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.Classes.Album
import com.example.ougimusic.Classes.Queue
import com.example.ougimusic.Classes.Song
import com.example.ougimusic.utilities.AdapterSongsList
import com.example.ougimusic.utilities.ContextVariables
import com.example.ougimusic.utilities.ResponseMessages
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class CancionesAlbum : AppCompatActivity() {

    val client = OkHttpClient()
    val gson = GsonBuilder().create()
    val songDataList = mutableListOf<Song>()
    val adapter = AdapterSongsList(songDataList)
    val global = ContextVariables()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canciones)
        val title = findViewById<TextView>(R.id.textViewPlaylistName)
        val intent = intent
        val album = intent.getSerializableExtra("album") as Album
        title.text = album.albumname
        val recycler: RecyclerView = findViewById<RecyclerView>(R.id.recyclerPlaylistSongs)
        recycler.layoutManager = LinearLayoutManager(parent, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter
        run {
            SearchAlbumSongs(album._id)
        }
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

    fun SearchAlbumSongs(_id:String?){
        val json = """
        {
        "albumId": "${_id}"
        }
    """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("${global.rootDirection}songs/getSongsByAlbum")
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        "No se pudo conectar a la base de datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "Existe un error de tipo: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    runOnUiThread {
                        val bodyResponse = response.body!!.string()
                        val jsonResponse =
                            gson.fromJson(bodyResponse, ResponseMessages.SongListResponse::class.java)
                        songDataList.addAll(jsonResponse.data);
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }
}