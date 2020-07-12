package com.example.ougimusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.Classes.Song
import com.example.ougimusic.utilities.AdapterSongsList
import com.example.ougimusic.utilities.ContextVariables
import com.example.ougimusic.utilities.ResponseMessages
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

lateinit var busqueda : TextView
val client = OkHttpClient()
val gson = GsonBuilder().create()
val songDataList = mutableListOf<Song>()
val adapter = AdapterSongsList(songDataList)
val global = ContextVariables()

class Buscar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)

        busqueda = findViewById(R.id.editTextTextPersonName2)

        val recycler: RecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(parent, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter

        buttonRegresar.setOnClickListener {
            val intent: Intent = Intent(this, InicioReproductor::class.java)
            startActivity(intent)
            finish()
        }
    }



    fun buscarCancion (v: View){

        var text = busqueda.text.toString()
        ReadData(text)
    }

    fun ReadData(songName: String?){

        val json = """
            {
            "name": "${songName}"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("${global.rootDirection}songs/getSongByName")
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
                        Log.d("name", response.toString())

                    }
                } else {
                    runOnUiThread {
                        val bodyResponse = response.body!!.string()
                        val jsonResponse =
                            gson.fromJson(bodyResponse, ResponseMessages.searchResponse::class.java)
                        var songs:List<Song> = jsonResponse.data

                        if (songs.isEmpty()){
                            Toast.makeText(
                                applicationContext,
                                "No se encontró canción",
                                Toast.LENGTH_SHORT)
                        }else{
                            for (song in songs){
                                songDataList.add(song)
                            }
                            adapter.notifyDataSetChanged()
                        }

                    }
                }
            }

        })

    }
}