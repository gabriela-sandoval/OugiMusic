package com.example.ougimusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ougimusic.Classes.Queue
import com.example.ougimusic.Classes.Song
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val song = Song()
        song.title = "Señor locutor"
        song.urlStreaming = "http://192.168.1.73:8000/"
        song.urlImage = "https://i.imgur.com/C1oQNyC.jpg"
        song.album = "Grandes éxitos"
        song.artist = "los tigres del norte"

        val song2 = Song()
        song2.title = "Good Riddance"
        song2.urlStreaming = "http://192.168.1.73:8000/"
        song2.urlImage = "https://i.imgur.com/s6ru3Ce.jpg"
        song2.album = "Nimrod"
        song2.artist = "Green day"


        val song3 = Song()
        song3.title = "Everlong"
        song3.urlStreaming = "http://192.168.1.73:8000/"
        song3.urlImage = "https://i.imgur.com/0IhdOsj.jpg"
        song3.album = "The color and the shape"
        song3.artist = "Foo Fighters"




        var queue = Queue()
        queue.currentList?.add(song)
        queue.currentList?.add(song2)
        queue.currentList?.add(song3)
        queue.currentSongPosition = 0




        buttonIngresar.setOnClickListener {
            val intent: Intent = Intent(this, InicioReproductor::class.java)


            intent.putExtra("Current_List", queue )

            startActivity(intent)
            finish()
        }

        buttonRegistrarse.setOnClickListener {
            val intent: Intent = Intent(this, RegistrarUsuario::class.java)
            startActivity(intent)
            finish()
        }
    }
}