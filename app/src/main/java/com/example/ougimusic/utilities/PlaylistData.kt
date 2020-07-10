package com.example.ougimusic.utilities

import android.icu.text.CaseMap
import android.os.Parcelable
import com.example.ougimusic.Albumes
import java.io.Serializable

class PlaylistData : Serializable{

    data class Response(
        val status: String,
        val data: List<Playlist>
    )

    data class Playlist (
        val _id: String,
        val songs: ArrayList<String>,
        val user: String,
        val name: String
    )
    data class SongResponse(
        val _id: String,
        val data: Song
    )
    data class Song(
        val title: String,
        val number: Double,
        val album: String,
        val albumId: String,
        val artist: String,
        val artistId: String,
        val genre: String,
        val year: String,
        val urlStreaming: String,
        val urlImage: String
    )
}