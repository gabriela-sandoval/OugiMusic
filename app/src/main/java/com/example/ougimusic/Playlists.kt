package com.example.ougimusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import kotlinx.android.synthetic.main.playlist_list_view.*

class Playlists : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlists)

        buttonRegresar.setOnClickListener {
            finish()
        }

        layoutPlayList.setOnClickListener {
            val intent = Intent(this, Canciones::class.java)
            startActivity(intent)
        }
    }
}