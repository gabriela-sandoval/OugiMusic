package com.example.ougimusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonIngresar.setOnClickListener {
            val intent: Intent = Intent(this, InicioReproductor::class.java)
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