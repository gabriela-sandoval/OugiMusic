package com.example.ougimusic

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.Classes.Song
import com.example.ougimusic.utilities.AdapterPlaylist
import com.example.ougimusic.utilities.AdapterSongsList
import com.example.ougimusic.utilities.ContextVariables
import com.example.ougimusic.utilities.ResponseMessages
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.wait
import org.w3c.dom.Text
import java.io.IOException
import kotlin.coroutines.coroutineContext

class Canciones : AppCompatActivity() {

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
        title.text = intent.getStringExtra("playlistName")
        val songsList = intent.getStringArrayListExtra("songList")
        val recycler: RecyclerView = findViewById<RecyclerView>(R.id.recyclerPlaylistSongs)
        recycler.layoutManager = LinearLayoutManager(parent, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter
        ReadData(songsList)
        buttonRegresar.setOnClickListener {
            finish()
        }

    }

    fun ReadData(songsList: ArrayList<String>){
        songsList.forEach {
            val json = """
            {
            "songId": "$it"
            }
        """.trimIndent()
            val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("${global.rootDirection}songs/getSongId")
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
                                gson.fromJson(bodyResponse, ResponseMessages.SongResponse::class.java)
                            songDataList.add(jsonResponse.data)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

            })
        }
    }
}