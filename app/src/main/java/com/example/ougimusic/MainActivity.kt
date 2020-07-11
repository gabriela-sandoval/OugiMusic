package com.example.ougimusic

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ougimusic.utilities.AdapterPlaylist
import com.example.ougimusic.utilities.ContextVariables
import com.example.ougimusic.utilities.PlaylistData
import com.example.ougimusic.utilities.UserCredentials
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.wait
import java.io.IOException
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


        buttonIngresar.setOnClickListener {
            if (CheckEnteredData()) {
                var loginResult = false
                val job = GlobalScope.launch {
                     DoLogin {
                        println(it)
                         loginResult = it
                    }
                }
                Toast.makeText(applicationContext, "Cargando", Toast.LENGTH_SHORT).show()
                while (job.isActive){

                }

                if (loginResult){
                    if (checkboxRememberMe.isChecked){
                        // Repair this
                        val intent = Intent(this, InicioReproductor::class.java)
                        startActivity(intent)
                        finish()
                    }
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

    fun DoLogin(onSuccess: (loginResult: Boolean) -> Unit) {
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
                onSuccess(result)
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
    }
}