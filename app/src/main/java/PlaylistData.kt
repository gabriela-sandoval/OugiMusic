class PlaylistData {

    data class Respose(
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