package org.wit.musiclist.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class SongMemStore : SongStore, AnkoLogger {

    val songs = ArrayList<SongModel>()

    override fun findAll(): List<SongModel> {
        return songs
    }

    override fun create(song: SongModel) {
        song.id = getId()
        songs.add(song)
        logAll()
    }

    override fun update(song: SongModel) {
        var foundSong: SongModel? = songs.find { p -> p.id == song.id }
        if (foundSong != null) {
            foundSong.title = song.title
            foundSong.artist = song.artist
            foundSong.image = song.image
            foundSong.audio = song.audio

            logAll()
        }
    }

    override fun delete(song: SongModel) {
        songs.remove(song)
    }

    fun logAll() {
        songs.forEach { info("${it}") }
    }

    override fun clear() {
        songs.clear()
    }


}