package com.example.ougimusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.ougimusic.Classes.Genre
import com.example.ougimusic.utilities.AdapterGenres
import kotlinx.android.synthetic.main.activity_registrar_usuario.*


lateinit var listGenero : ListView


class Generos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generos)

        listGenero = findViewById(R.id.listViewGeneros) as ListView
        var listGeneros : ArrayList<Genre> = ArrayList()
        listGeneros.add(Genre("Rock", R.drawable.rock))
        listGeneros.add(Genre("Pop", R.drawable.pop))
        listGeneros.add(Genre("Hip-Hop", R.drawable.hiphop))
        listGeneros.add(Genre("Electronic", R.drawable.electronic))
        listGeneros.add(Genre("Classical", R.drawable.clasical))
        listGeneros.add(Genre("Metal", R.drawable.metal))
        listGeneros.add(Genre("Blues", R.drawable.blues))
        listGeneros.add(Genre("Latin", R.drawable.latin))
        listGeneros.add(Genre("Indie", R.drawable.indie))


        listGenero.adapter = AdapterGenres(applicationContext, listGeneros)


        buttonRegresar.setOnClickListener {
            val intent: Intent = Intent(this, InicioReproductor::class.java)
            startActivity(intent)
            finish()
        }
    }
}