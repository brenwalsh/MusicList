package org.wit.musiclist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class SongModel(var id: Long = 0,
                     var fbId : String = "",
                     var image: String = "",
                     var audio: String = "",
                     var title: String = "",
                     var artist: String = "") : Parcelable
