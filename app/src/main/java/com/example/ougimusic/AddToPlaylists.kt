package com.example.ougimusic

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.Classes.Playlist
import com.example.ougimusic.utilities.AdapterAddToPlaylist
import com.example.ougimusic.utilities.AdapterPlaylist
import com.example.ougimusic.utilities.ContextVariables
import com.example.ougimusic.utilities.ResponseMessages
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_playlists.*
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import kotlinx.android.synthetic.main.activity_registrar_usuario.buttonRegresar
import kotlinx.android.synthetic.main.layout_new_playlist.*
import kotlinx.android.synthetic.main.layout_new_playlist.view.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class AddToPlaylists : AppCompatActivity() {
    private val client = OkHttpClient()
    private val gson = GsonBuilder().create()
    private val global = ContextVariables()
    private val playlistList = mutableListOf<Playlist>()
    private var adapter = AdapterAddToPlaylist(playlistList, this, "DefaulString")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AdapterAddToPlaylist(playlistList, this, intent.getStringExtra("songId"))
        setContentView(R.layout.activity_add_playlists)
        val recycler = findViewById<RecyclerView>(R.id.recyclePlaylist)
        recycler.layoutManager = LinearLayoutManager(parent, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter
        run{
            GetPlaylist()
        }
        buttonRegresar.setOnClickListener {
            finish()
        }
    }


    fun OpenDialog(){
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater?.inflate(R.layout.layout_new_playlist, null)
        builder.setView(view)
            .setTitle("Crear nueva lista")
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->

            })
            .setPositiveButton("Crear", DialogInterface.OnClickListener { dialog, which ->
                run{
                    CreatePlaylist(view.playlistName.text.toString())
                }
            })
        builder.create().show()
    }

    fun GetPlaylist(){
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
                        playlistList.clear()
                        playlistList.addAll(jsonResponse.data)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }
    fun CreatePlaylist(listName: String){
        val userPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val username = userPreferences.getString("username", "")
        val json = """
            {
            "user": "$username",
            "name": "$listName"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("${global.rootDirection}playlist/CreatePlaylist")
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    Toast.makeText(applicationContext, "No se pudo crear la lista de reproduccion", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if(!response.isSuccessful){
                    runOnUiThread{
                        Toast.makeText(applicationContext, "Existe un error de tipo: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    runOnUiThread{
                        Toast.makeText(applicationContext, "Lista creada", Toast.LENGTH_SHORT).show()
                        GetPlaylist()
                    }
                }
            }
        })
    }
}