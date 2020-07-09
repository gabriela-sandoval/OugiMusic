package com.example.ougimusic

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class InicioReproductor : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_reproductor)

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
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inicio -> {
                val intent = Intent(this, InicioReproductor::class.java)
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