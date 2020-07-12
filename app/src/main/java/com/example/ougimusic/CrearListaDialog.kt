package com.example.ougimusic

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Layout
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.layout_new_playlist.*
import java.lang.ClassCastException

class CrearListaDialog() : AppCompatDialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.layout_new_playlist, null)
        builder.setView(view)
            .setTitle("Crear nueva lista")
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->

            })
            .setPositiveButton("Crear", DialogInterface.OnClickListener { dialog, which ->
                playlistName.text
            })
        return builder.create()
    }

    interface DialogListener{
        fun applyText(name:String)
    }
}
