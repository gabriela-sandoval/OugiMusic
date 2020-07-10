package com.example.ougimusic.utilities

class PlaylistData {

    data class Response(
        val status: String,
        val data: List<Playlist>
    )

    data class Playlist(
        val _id: String,
        val songs: List<String>,
        val user: String,
        val name: String
    )
}