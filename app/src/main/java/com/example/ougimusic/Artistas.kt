package com.example.ougimusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.Classes.Artist
import com.example.ougimusic.Classes.Song
import com.example.ougimusic.utilities.AdapterArtist
import com.example.ougimusic.utilities.AdapterSongsList
import com.example.ougimusic.utilities.ContextVariables
import com.example.ougimusic.utilities.ResponseMessages
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import okhttp3.*
import java.io.IOException


class Artistas : AppCompatActivity() {

    val client = OkHttpClient()
    val gson = GsonBuilder().create()
    val ArtistDataList = mutableListOf<Artist>()
    val adapter = AdapterArtist(ArtistDataList)
    val global = ContextVariables()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artistas)




        val recycler: RecyclerView = findViewById<RecyclerView>(R.id.recyclerPlaylistSongs)
        recycler.layoutManager = LinearLayoutManager(parent, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter
        ReadData()



        buttonRegresar.setOnClickListener {
            val intent: Intent = Intent(this, InicioReproductor::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun ReadData() {

        val request = Request.Builder()
            .url("${global.rootDirection}songs/getAllArtists")
            .get()
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
                            gson.fromJson(bodyResponse, ResponseMessages.artistResponse::class.java)
                        Log.d("data", jsonResponse.data[1].artist)
                        ArtistDataList.addAll(jsonResponse.data)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })

    }
}