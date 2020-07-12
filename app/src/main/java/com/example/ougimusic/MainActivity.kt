package com.example.ougimusic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ougimusic.Classes.Queue
import com.example.ougimusic.Classes.Song
import com.example.ougimusic.utilities.ContextVariables
import com.example.ougimusic.utilities.UserCredentials
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.ConnectException
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private val emailPattern = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}\$".toRegex()
    private val client = OkHttpClient()
    private val gson = GsonBuilder().create()
    val global = ContextVariables()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      
        val preferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val username = preferences.getString("username", "")
        val preferencesCheckBox = getSharedPreferences("checkbox", Context.MODE_PRIVATE)
        val checkBox = preferences.getString("remember", "")

        if (checkBox.equals("true") && !username.equals("")) {
            val intent = Intent(this, InicioReproductor::class.java)
            startActivity(intent)
            finish()
        }

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

        //(this.application as ContextVariables).setQueue(queue)






        buttonIngresar.setOnClickListener {
            val intent = Intent(this, InicioReproductor::class.java)


            intent.putExtra("Current_List", queue )
            startActivity(intent)
            finish()
        }


        buttonIngresar.setOnClickListener {
            if (CheckEnteredData()) {
                var loginResult: Boolean


                runBlocking (Dispatchers.Default){
                    loginResult = DoLogin()
                }
                Toast.makeText(applicationContext, "Cargando", Toast.LENGTH_SHORT).show()

                if (loginResult){
                    val intent = Intent(this, InicioReproductor::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }


        buttonRegistrarse.setOnClickListener {
            val intent = Intent(this, RegistrarUsuario::class.java)
            startActivity(intent)
        }

        checkboxRememberMe.setOnCheckedChangeListener { checkboxRememberMe, isChecked ->
            val editor = preferencesCheckBox.edit()
            if (checkboxRememberMe.isChecked) {
                editor.putString("remember", "true")
                editor.apply()
            } else if (!checkboxRememberMe.isChecked) {
                editor.putString("remember", "false")
                editor.apply()
                Toast.makeText(applicationContext, "$checkBox", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun CheckEnteredData(): Boolean {
        var boolean = false
        if (editTextTextPassword.text.isEmpty() ||
            editTextUsername.text.isEmpty()
        ) {
            Toast.makeText(applicationContext, "Ingresa todos los datos", Toast.LENGTH_SHORT).show()
        } else {
            boolean = true
        }
        return boolean
    }

    fun DoLogin(): Boolean {
        val username = editTextUsername.text
        val password = editTextTextPassword.text
        val json = """
            {
            "username": "$username",
            "password": "$password"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        var result = false
        val request = Request.Builder()
            .url("${global.rootDirection}login")
            .post(body)
            .build()
        try{
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    when (response.code) {
                        403 ->
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Usuario o contraseña incorrectos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        401 ->
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Usuario no encontrado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        500 ->
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Error de nuestra parte, reintentar en unos momentos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    val bodyResponse = response.body!!.string()
                    val jsonResponse = gson.fromJson(bodyResponse, UserCredentials::class.java)

                    val preferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                    val editor = preferences.edit()

                    editor.putString("username", "$username")
                    editor.putString("token", "${jsonResponse.token}")
                    editor.apply()
                    result = true
                }
            }
        }catch (exception:ConnectException){
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "Error al conectar al servidor, reintentar mas tarde",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }catch (exception:SocketTimeoutException){
            runOnUiThread {
                Toast.makeText(
                    applicationContext,
                    "El servidor tardo mas de lo esperado en responder",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return result
    }
}