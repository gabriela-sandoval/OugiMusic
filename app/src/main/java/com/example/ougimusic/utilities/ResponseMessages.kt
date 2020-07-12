package com.example.ougimusic.utilities

import android.icu.text.CaseMap
import android.os.Parcelable
import com.example.ougimusic.Albumes
import com.example.ougimusic.Classes.Playlist
import com.example.ougimusic.Classes.Song
import java.io.Serializable

class ResponseMessages : Serializable{

    data class PlaylistResponse(
        val status: String,
        val data: List<Playlist>
    )

    data class SongResponse(
        val _id: String,
        val data: Song
    )

    data class GenreResponse(
        val status: String,
        val data: List<Song>
    )
}