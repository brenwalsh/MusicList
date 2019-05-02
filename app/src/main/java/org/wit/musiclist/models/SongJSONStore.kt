package org.wit.musiclist.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.musiclist.helpers.*
import java.util.*

val JSON_FILE = "songs.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<SongModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class SongJSONStore : SongStore, AnkoLogger {

    val context: Context
    var songs = mutableListOf<SongModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<SongModel> {
        return songs
    }

    override fun create(placemark: SongModel) {
        placemark.id = generateRandomId()
        songs.add(placemark)
        serialize()
    }


    override fun update(song: SongModel) {
        val songsList = findAll() as ArrayList<SongModel>
        var foundSong: SongModel? = songsList.find { p -> p.id == song.id }
        if (foundSong != null) {
            foundSong.title = song.title
            foundSong.artist = song.artist
            foundSong.image = song.image
            foundSong.audio = song.audio

        }
        serialize()
    }

    override fun delete(song: SongModel) {
        songs.remove(song)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(songs, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        songs = Gson().fromJson(jsonString, listType)
    }

    override fun clear() {
        songs.clear()
    }
}