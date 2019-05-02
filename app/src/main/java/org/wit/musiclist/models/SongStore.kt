package org.wit.musiclist.models

interface SongStore {
    fun findAll(): List<SongModel>
    fun create(song: SongModel)
    fun update(song: SongModel)
    fun delete(song: SongModel)
    fun clear()
}