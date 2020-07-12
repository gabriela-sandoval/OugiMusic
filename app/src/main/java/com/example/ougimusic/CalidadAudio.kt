package com.example.ougimusic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import kotlinx.android.synthetic.main.activity_calidad_audio.*
import kotlinx.android.synthetic.main.activity_registrar_usuario.*

class CalidadAudio : AppCompatActivity() {
    internal lateinit var switchAlta: Switch
    internal lateinit var switchMedia: Switch
    internal lateinit var switchBaja: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calidad_audio)

        buttonRegresar2.setOnClickListener {
            finish()
        }

        switchAlta = findViewById<View>(R.id.switchCalidadAlta) as Switch
        switchMedia = findViewById<View>(R.id.switchCalidadMedia) as Switch
        switchBaja = findViewById<View>(R.id.switchCalidadBaja) as Switch

        switchAlta.setOnClickListener{
            if(switchAlta.isChecked)
            {
                switchMedia.isChecked = false
                switchBaja.isChecked = false
            } else if(switchMedia.isChecked) {
                switchAlta.isChecked = false
                switchBaja.isChecked = false
            } else if (switchBaja.isChecked)
            {
                switchMedia.isChecked = false
                switchAlta.isChecked = false
            }
        }
    }
}