package com.example.ougimusic

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PersistableBundle
import android.util.Log
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
import com.example.ougimusic.utilities.ContextVariables
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import java.io.IOException


class InicioReproductor : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MediaPlayer.OnCompletionListener {

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
    var queue:Queue? = null
    private val global = ContextVariables()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_reproductor)
        barraProgreso = findViewById(R.id.barraProgreso)
        botonPlay = findViewById(R.id.botonPlayGenre)
        botonAnterior = findViewById(R.id.botonAnterior)
        botonSiguiente = findViewById(R.id.botonSiguiente)
        textViewInicioCancion = findViewById(R.id.textViewInicioCancion)
        textViewFinCancion = findViewById(R.id.textViewFinCancion)
        textViewNombreCanción = findViewById(R.id.textViewNombreCanción)
        textViewNombreArtista = findViewById(R.id.textViewNombreGenero)
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

        queue = intent.getSerializableExtra("Current_List") as? Queue

        //queue = (this.application as ContextVariables).getQueue()



        if(queue != null){
            position = queue?.currentSongPosition!!
            song = queue?.currentList?.get(this.position)

            mp = MediaPlayer()
            playSong(urlStreaming = song?.urlStreaming)
            updateSongInfo(song?.title, song?.artist)
            updateSongArt(song)

            (this.application as ContextVariables).addSongQueue(song)

            var queue = (this.application as ContextVariables).getQueue()

            Log.d("canciones" , queue?.currentList?.get(0)?.title)

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

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
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

    fun updateSongArt(song: Song?){
        Picasso.get().load(song?.urlImage).into(songImage);
    }

    fun playSong(urlStreaming: String?) {


        if (queue != null){
            try{
                mp!!.setDataSource(urlStreaming)
                mp!!.prepare()
                totalTime = mp!!.duration
                mp!!.start()
                mp!!.setOnCompletionListener(this)


            }catch(e: IOException){
                Toast.makeText(this, "mp3 no encontrado", Toast.LENGTH_SHORT).show();
            }
        }

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

        if(queue != null) {

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


    }


    fun nextSong(v: View){
        nextSongOnEnd()

    }

    fun nextSongOnEnd(){
        if(queue != null){
            var queueSize = queue?.currentList?.size
            if( position+1 < queueSize!!) {
                position++
                var nextSong = queue?.currentList?.get(position)
                song = nextSong
                mp?.reset()
                updateSongInfo(song?.title, song?.artist)
                updateSongArt(song)
                playSong(song?.urlStreaming)
            }else{
                Toast.makeText(this, "No hay más canciones", Toast.LENGTH_SHORT).show();
            }
        }
    }

    fun previousSong(v: View){

        if(queue != null) {
            var queueSize = queue?.currentList?.size
            if( position+1 <= queueSize!! && position != 0) {
                position--
                var nextSong = queue?.currentList?.get(position)
                song = nextSong
                mp?.reset()
                updateSongInfo(song?.title, song?.artist)
                updateSongArt(song)
                playSong(song?.urlStreaming)
            }else {

                Toast.makeText(this, "No hay más canciones", Toast.LENGTH_SHORT).show();
            }
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inicio -> {
                mp?.stop()
                val intent: Intent = Intent(this, InicioReproductor::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_buscar -> {
                mp?.stop()
                val intent = Intent(this, Buscar::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_playlist -> {
                mp?.stop()
                val intent = Intent(this, Playlists::class.java)
                startActivity(intent)
            }
            R.id.nav_artistas -> {
                mp?.stop()
                val intent = Intent(this, Artistas::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_albumes -> {
                mp?.stop()
                val intent = Intent(this, Albumes::class.java)
                startActivity(intent)
            }
            R.id.nav_canciones -> {
                mp?.stop()
                val intent = Intent(this, Canciones::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_generos -> {
                mp?.stop()
                val intent = Intent(this, Generos::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_descargas -> {
                mp?.stop()
                val intent = Intent(this, Canciones::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_historial_reproduccion -> {
                mp?.stop()
                val intent = Intent(this, CancionesHistorial::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_calidad_audio-> {
                mp?.stop()
                val intent = Intent(this, CalidadAudio::class.java)
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.d("Completion Listener","Song Complete")
        nextSongOnEnd()
    }
}