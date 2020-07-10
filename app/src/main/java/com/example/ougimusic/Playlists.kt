package com.example.ougimusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.ContentView
import com.example.ougimusic.utilities.PlaylistData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import kotlinx.android.synthetic.main.playlist_list_view.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.w3c.dom.Element
import java.io.IOException

class Playlists : AppCompatActivity() {
    private val client = OkHttpClient()
    private val gson = GsonBuilder().create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlists)

        buttonRegresar.setOnClickListener {
            finish()
        }

        val json = """
            {
            "user": "Batman"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("http://192.168.0.19/playlist/getMyPlaylist")
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    Toast.makeText(applicationContext, "No se pudieron obtener tus listas de reproduccion", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if(!response.isSuccessful){
                    runOnUiThread{
                        Toast.makeText(applicationContext, "Existe un error de tipo: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }else{

                    val bodyResponse = response.body!!.string()
                    val jsonResponse = gson.fromJson(bodyResponse, PlaylistData.Response::class.java)
                    jsonResponse.data.forEach{
                        var tempPlaylist =  R.layout.playlist_list_view

                    }
                }
            }
        })
    }
}