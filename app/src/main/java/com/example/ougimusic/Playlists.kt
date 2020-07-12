package com.example.ougimusic

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.utilities.AdapterPlaylist
import com.example.ougimusic.utilities.ContextVariables
import com.example.ougimusic.utilities.ResponseMessages
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Playlists : AppCompatActivity() {
    private val client = OkHttpClient()
    private val gson = GsonBuilder().create()
    private val global = ContextVariables()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlists)
        run{
            getSongsPlaylist()
        }
        buttonRegresar.setOnClickListener {
            finish()
        }
    }

    fun getSongsPlaylist(){
        val userPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val username = userPreferences.getString("username", "")
        val json = """
            {
            "user": "$username"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("${global.rootDirection}playlist/getMyPlaylist")
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
                    val jsonResponse = gson.fromJson(bodyResponse, ResponseMessages.PlaylistResponse::class.java)
                    runOnUiThread{
                        val recycler = findViewById<RecyclerView>(R.id.recyclePlaylist)
                        recycler.layoutManager = LinearLayoutManager(parent, RecyclerView.VERTICAL, false)
                        val adapter = AdapterPlaylist(jsonResponse.data)
                        recycler.adapter = adapter
                    }
                }
            }
        })
    }
}