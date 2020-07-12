package com.example.ougimusic.utilities


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.ougimusic.Canciones
import com.example.ougimusic.Classes.Queue
import com.example.ougimusic.Classes.Song
import com.example.ougimusic.InicioReproductor
import com.example.ougimusic.R
import com.example.ougimusic.utilities.ContextVariables
import com.squareup.picasso.Picasso


class AdapterSongsList(var list: List<Song>) : Adapter<AdapterSongsList.SongsViewHolder>() {


    class SongsViewHolder(view:View): RecyclerView.ViewHolder(view){

        fun bindItem(data: Song){
            val title = itemView.findViewById<TextView>(R.id.textViewNombreCancion)
            title.text = data.title
            val artist = itemView.findViewById<TextView>(R.id.textViewArtista)
            artist.text = data.artist
            val album = itemView.findViewById<TextView>(R.id.textViewAlbum)
            album.text = data.album
            val año = itemView.findViewById<TextView>(R.id.textViewAño)
            año.text = data.year
            val songImage = itemView.findViewById<ImageView>(R.id.generoImage)
            Picasso.get().load(data?.urlImage).into(songImage);


            itemView.setOnClickListener{

                var queue = Queue()
                queue.currentList?.add(data)
                queue.currentSongPosition = 0
                val intent = Intent(it.context, InicioReproductor::class.java)
                intent.putExtra("Current_List", queue)
                it.context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val viewSave = LayoutInflater.from(parent.context).inflate(R.layout.cancion_list_view, parent, false)
        return SongsViewHolder(viewSave)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bindItem(list[position])
    }
}