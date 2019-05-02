package org.wit.musiclist.helpers

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import java.io.IOException
import android.net.Uri
import org.jetbrains.anko.info
import org.jetbrains.anko.AnkoLogger




fun showAudioPicker(parent: Activity, id: Int) {
    val intent = Intent()
    intent.type = "audio/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val chooser = Intent.createChooser(intent, org.wit.musiclist.R.string.select_song_audio.toString())
    parent.startActivityForResult(chooser, id)

}

fun readAudio(activity: Activity, resultCode: Int, data: Intent?): Uri? {
    var uri: Uri?= null
    if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
        try {
            var uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return uri

}
