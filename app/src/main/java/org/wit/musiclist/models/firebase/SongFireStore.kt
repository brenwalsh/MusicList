package org.wit.musiclist.models.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.musiclist.models.SongModel
import org.wit.musiclist.models.SongStore

class SongFireStore(val context: Context) : SongStore, AnkoLogger {

    val songs = ArrayList<SongModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun findAll(): List<SongModel> {
        return songs
    }

    fun findById(id: Long): SongModel? {
        val foundSong: SongModel? = songs.find { p -> p.id == id }
        return foundSong
    }

    override fun create(song: SongModel) {
        val key = db.child("users").child(userId).child("songs").push().key
        song.fbId = key!!
        songs.add(song)
        db.child("users").child(userId).child("songs").child(key).setValue(song)
    }

    override fun update(song: SongModel) {
        var foundSong: SongModel? = songs.find { p -> p.fbId == song.fbId }
        if (foundSong != null) {
            foundSong.title = song.title
            foundSong.artist = song.artist
            foundSong.image = song.image
            foundSong.audio = song.audio
        }

        db.child("users").child(userId).child("songs").child(song.fbId).setValue(song)
    }

    override fun delete(song: SongModel) {
        db.child("users").child(userId).child("songs").child(song.fbId).removeValue()
        songs.remove(song)
    }

    override fun clear() {
        songs.clear()
    }

    fun fetchSongs(songsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(songs) { it.getValue<SongModel>(SongModel::class.java) }
                songsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        songs.clear()
        db.child("users").child(userId).child("songs").addListenerForSingleValueEvent(valueEventListener)
    }
}