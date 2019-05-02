package org.wit.musiclist.activities

import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import org.jetbrains.anko.toast
import org.wit.musiclist.R
import java.net.URI

@Suppress("UNREACHABLE_CODE")
class MediaActivity: AppCompatActivity () {
    private lateinit var mp: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        /*val bundle:Bundle = intent.extras
        val audioURI: String = bundle.get("audioUri") as String
        val myUri = Uri.parse(audioURI)*/

        // coding180.com

        mp = MediaPlayer.create(this, R.raw.song1)

        var position = 0

        val playButton = findViewById<Button>(R.id.playButton)
        val pauseButton = findViewById<Button>(R.id.pauseButton)
        val stopButton = findViewById<Button>(R.id.stopButton)


        playButton.setOnClickListener {
            mp.start()

        }

        pauseButton.setOnClickListener {
            if (mp.isPlaying()) {
                position = mp.getCurrentPosition()
                mp.pause()
            }
        }

        stopButton.setOnClickListener {
            mp.pause()
            position = 0
            mp.seekTo(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mp.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_media, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.media_return -> finish()
            }
            return super.onOptionsItemSelected(item)
        }
    }