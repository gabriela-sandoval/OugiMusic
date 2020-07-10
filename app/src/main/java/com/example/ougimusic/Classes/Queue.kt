package com.example.ougimusic.Classes
import java.io.Serializable

public class Queue : Serializable  {
    var currentList: MutableList<Song>? = ArrayList()
    var currentSongPosition: Int? = -1

}