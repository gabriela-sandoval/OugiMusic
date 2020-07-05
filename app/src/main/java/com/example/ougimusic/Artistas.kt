package com.example.ougimusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_registrar_usuario.*

class Artistas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artistas)

        buttonRegresar.setOnClickListener {
            val intent: Intent = Intent(this, InicioReproductor::class.java)
            startActivity(intent)
            finish()
        }
    }
}