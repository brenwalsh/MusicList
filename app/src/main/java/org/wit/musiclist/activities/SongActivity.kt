package org.wit.musiclist.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.activity_song.*
import org.wit.musiclist.R
import org.wit.musiclist.helpers.readImage
import org.wit.musiclist.helpers.showAudioPicker
import org.wit.musiclist.helpers.showImagePicker
import org.wit.musiclist.main.MainApp
import org.wit.musiclist.models.SongModel


class SongActivity :AppCompatActivity(), AnkoLogger {

    var song =  SongModel()
    lateinit var app: MainApp
    var edit = false
    val IMAGE_REQUEST = 1
    val AUDIO_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)

        }
        chooseAudio.setOnClickListener {
            showAudioPicker(this, AUDIO_REQUEST)
        }

        startMedia.setVisibility(View.INVISIBLE);



        if (intent.hasExtra("song_edit")) {
            edit = true
            startMedia.setVisibility(View.VISIBLE)

            song = intent.extras.getParcelable<SongModel>("song_edit")
            songTitle.setText(song.title)
            description.setText(song.artist)
            btnAdd.setText(R.string.button_updateSong)
            Glide.with(this).load(song.image).into(songImage)
            if (song.image != null) {
                chooseImage.setText(R.string.change_song_image)
            }

            startMedia.setOnClickListener{
                intent = Intent(this, MediaActivity::class.java)
                /*val audioURI = song.audio
               intent.putExtra("audioURI", audioURI)
                if (audioURI == "") {
                    toast("ERROR")
                }*/
                startActivity(intent)

            }

        }


        btnAdd.setOnClickListener() {
            song.title = songTitle.text.toString()
            song.artist = description.text.toString()
            if (song.title.isEmpty()) {
                toast(R.string.enter_song_title)
            } else {
                if (edit) {
                    app.songs.update(song.copy())
                } else {
                    app.songs.create(song.copy())
                }
            }
            info("add Button Pressed: $songTitle")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_song, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                app.songs.delete(song)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    song.image = data.getData().toString()
                    songImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_song_image)
                }
            }

        }
        when (requestCode) {
            AUDIO_REQUEST -> {
                if (data != null) {
                    song.audio = data.getData().toString()
                    chooseAudio.setText(R.string.change_song_audio)


                }
            }
        }
    }
}

