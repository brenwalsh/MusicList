package org.wit.musiclist.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_song.view.*
import org.wit.musiclist.R
import org.wit.musiclist.models.SongModel

interface SongListener {

    fun onSongClick(song: SongModel)
}

class SongAdapter constructor(private var songs: ArrayList<SongModel>,
                                   private val listener: SongListener) : RecyclerView.Adapter<SongAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_song, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val song = songs[holder.adapterPosition]
        holder.bind(song, listener)
    }

    override fun getItemCount(): Int = songs.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(song: SongModel,  listener : SongListener) {
            itemView.songTitle.text = song.title
            itemView.description.text = song.artist
            Glide.with(itemView.context).load(song.image).into(itemView.imageIcon)
            itemView.setOnClickListener { listener.onSongClick(song) }
        }
    }
}