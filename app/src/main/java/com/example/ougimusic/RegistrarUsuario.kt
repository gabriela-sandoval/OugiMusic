package com.example.ougimusic

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ougimusic.utilities.ContextVariables
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class RegistrarUsuario : AppCompatActivity() {

    private val client = okhttp3.OkHttpClient()
    private val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}\$".toRegex()
    private val emailPattern = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}\$".toRegex()
    val global = ContextVariables()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        buttonRegresar.setOnClickListener {
            finish()
        }
        buttonRegistrarme.setOnClickListener{
            checkEnteredData()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    fun checkEnteredData(){
        if (editTextTextPassword2.text.isEmpty() ||
            editTextTextPassword3.text.isEmpty() ||
            editTextTextEmailAddress2.text.isEmpty() ||
            editTextTextPersonName.text.isEmpty()
                ){
            Toast.makeText(applicationContext, "Ingresa todos los datos", Toast.LENGTH_SHORT).show()
        }else if (editTextTextPassword2.text.toString() != editTextTextPassword3.text.toString()){
                Toast.makeText(applicationContext, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        }else if (!passwordPattern.containsMatchIn(editTextTextPassword2.text.toString())){
            Toast.makeText(applicationContext, "Contraseña no segura \n Comprueba que cuente con Mayúsculas, Minúsculas, números y una extensión de 8-32 caracteres.", Toast.LENGTH_SHORT).show()
        }else if(!emailPattern.containsMatchIn(editTextTextEmailAddress2.text.toString())){
            Toast.makeText(applicationContext, "Email no valido", Toast.LENGTH_SHORT).show()
        }
        else{
            registerUser()
        }
    }

    fun registerUser(){
        val json = """
            {
            "username": "${editTextTextPersonName.text}",
            "password": "${editTextTextPassword2.text}",
            "email": "${editTextTextEmailAddress2.text}"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())


        val request = Request.Builder()
                .url("${global.rootDirection}create")
                .post(body)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    val failure = AlertDialog.Builder(this@RegistrarUsuario)
                    failure.setTitle("Error")
                    failure.setMessage("Error al conectarse con la base de datos \n Reintentar mas tarde")

                    failure.setPositiveButton("Ok") { dialog, _ ->
                        dialog.cancel()
                    }
                    failure.show() }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful){
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Error al registrar \n Revisa tus datos y reintentalo", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        runOnUiThread {
                            val failure = AlertDialog.Builder(this@RegistrarUsuario)
                            failure.setTitle("Registrado!!")
                            failure.setMessage("Registrado correctamente \n Puedes ingresar al sistema")

                            failure.setPositiveButton("Ok") { dialog, _ ->
                                dialog.cancel()
                                finish()
                            }
                            failure.show() }
                    }

                }
            }
        })
    }

}