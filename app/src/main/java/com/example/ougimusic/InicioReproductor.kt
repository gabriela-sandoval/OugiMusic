package com.example.ougimusic

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ougimusic.Classes.Queue
import com.example.ougimusic.Classes.Song
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import java.io.IOException
import java.io.InputStream
import java.net.URL


class InicioReproductor : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    var mp: MediaPlayer? = null
    lateinit var botonPlay: Button
    lateinit var botonSiguiente : Button
    lateinit var botonAnterior : Button
    lateinit var barraProgreso: SeekBar
    lateinit var textViewInicioCancion : TextView
    lateinit var textViewFinCancion : TextView
    lateinit var textViewNombreArtista : TextView
    lateinit var textViewNombreCanción : TextView
    lateinit var songImage: ImageView
    private var totalTime: Int = 0
    var position = 0
    var song: Song? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_reproductor)

        barraProgreso = findViewById(R.id.barraProgreso)
        botonPlay = findViewById(R.id.botonPlay)
        botonAnterior = findViewById(R.id.botonAnterior)
        botonSiguiente = findViewById(R.id.botonSiguiente)
        textViewInicioCancion = findViewById(R.id.textViewInicioCancion)
        textViewFinCancion = findViewById(R.id.textViewFinCancion)
        textViewNombreCanción = findViewById(R.id.textViewNombreCanción)
        textViewNombreArtista = findViewById(R.id.textViewNombreArtista)
        songImage = findViewById(R.id.songImage)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)




        val queue = intent.getSerializableExtra("Current_List") as? Queue
        position = queue?.currentSongPosition!!
        song = queue?.currentList?.get(this.position)


        mp = MediaPlayer()
        playSong(urlStreaming = song?.urlStreaming)
        updateSongInfo(song?.title, song?.artist)


        Picasso.get().load(song?.urlImage).into(songImage);



        barraProgreso.max = totalTime
        barraProgreso.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser){
                        mp!!.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        Thread(Runnable {
            while (mp != null) {
                try {
                    var msg = Message()
                    msg.what = mp!!.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()


    }

    fun playSong(urlStreaming: String?) {

        try{
            mp!!.setDataSource(urlStreaming)
            mp!!.prepare()
            totalTime = mp!!.duration
            mp!!.start()
        }catch(e: IOException){
            Toast.makeText(this, "mp3 not found", Toast.LENGTH_SHORT).show();
        }
    }
    override fun onBackPressed() {
        val failure = AlertDialog.Builder(this)
        failure.setTitle("¿Desea Salir?")
        failure.setMessage("¿Desea Salir de la aplicacion?")

        failure.setPositiveButton("No") { dialog, _ ->
            dialog.cancel()
        }
        failure.setNegativeButton("Si") { dialog, _ ->
            dialog.cancel()
            finish()
        }
        failure.show()
    }
    fun updateSongInfo(title: String?, artist: String?) {
        textViewNombreCanción.text = title
        textViewNombreArtista.text = artist
    }


    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            barraProgreso.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeLabel(currentPosition)
            textViewInicioCancion.text = elapsedTime

            var totalTimeConverted = createTimeLabel(totalTime)
            textViewFinCancion.text = totalTimeConverted
        }
    }


    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

    fun playBtnClick(v: View) {

        if (this.mp?.isPlaying!!) {
            // Stop
            this.mp!!.pause()
            botonPlay.setBackgroundResource(R.drawable.play_button)
        } else {
            // Start
            this.mp!!.start()
            botonPlay.setBackgroundResource(R.drawable.pause)
        }
    }








    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inicio -> {
                mp?.reset()
                val intent: Intent = Intent(this, InicioReproductor::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_buscar -> {
                val intent = Intent(this, Buscar::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_playlist -> {
                val intent = Intent(this, Playlists::class.java)
                startActivity(intent)
            }
            R.id.nav_artistas -> {
                val intent = Intent(this, Artistas::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_albumes -> {
                val intent = Intent(this, Albumes::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_canciones -> {
                val intent = Intent(this, Canciones::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_generos -> {
                val intent = Intent(this, Generos::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_descargas -> {
                val intent = Intent(this, Canciones::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_historial_reproduccion -> {
                val intent = Intent(this, Canciones::class.java)
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}