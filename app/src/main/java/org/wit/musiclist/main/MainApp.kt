package org.wit.musiclist.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.musiclist.models.SongJSONStore
import org.wit.musiclist.models.SongMemStore
import org.wit.musiclist.models.SongModel
import org.wit.musiclist.models.SongStore
import org.wit.musiclist.models.firebase.SongFireStore

class MainApp : Application(), AnkoLogger {

    lateinit var songs: SongStore

    override fun onCreate() {
        super.onCreate()
        //songs = SongJSONStore(applicationContext)
        songs = SongFireStore(applicationContext)

        info("MusicList started")
    }
}