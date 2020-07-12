package com.example.ougimusic

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.Classes.Queue
import com.example.ougimusic.utilities.AdapterAlbum
import com.example.ougimusic.utilities.ContextVariables
import com.example.ougimusic.utilities.ResponseMessages
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AlbumesArtistas : AppCompatActivity() {

    private val client = OkHttpClient()
    private val gson = GsonBuilder().create()
    private val global = ContextVariables()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albumes)

        var  artist = intent.getSerializableExtra("Current_Artist") as? String


        val json = """
            {
            "artistName": "${artist}"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("${global.rootDirection}songs/getAlbumsByArtist")
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    Toast.makeText(applicationContext, "No se pudieron obtener albumes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if(!response.isSuccessful){
                    runOnUiThread{
                        Toast.makeText(applicationContext, "Existe un error de tipo: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }else{

                    val bodyResponse = response.body!!.string()
                    val jsonResponse = gson.fromJson(bodyResponse, ResponseMessages.AlbumResponse::class.java)
                    runOnUiThread{
                        val recycler = findViewById<RecyclerView>(R.id.recycleAlbum)
                        recycler.layoutManager = LinearLayoutManager(parent, RecyclerView.VERTICAL, false)
                        val adapter = AdapterAlbum(jsonResponse.data)
                        recycler.adapter = adapter
                    }
                }
            }
        })
        buttonRegresar.setOnClickListener {
            finish()
        }
    }
}