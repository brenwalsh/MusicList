package org.wit.musiclist.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_song_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.musiclist.R
import org.wit.musiclist.main.MainApp
import org.wit.musiclist.models.SongModel
import org.wit.musiclist.views.VIEW
import org.wit.musiclist.views.login.LoginView

class SongListActivity : AppCompatActivity(), SongListener {

    lateinit var app: MainApp
    companion object {
        private val songs = ArrayList<SongModel>()

        fun getLaunchIntent(from: Context) = Intent(from, SongListActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
    private var ascending = true


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_song_list)
            app = application as MainApp

            //layout and populate for display
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager   //recyclerView is a widget in activity_placemark_list.xml
            loadSongs()


        //enable action bar and set title
            toolbarMain.title = title
            setSupportActionBar(toolbarMain)
        }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onSongClick(song: SongModel) {
        startActivityForResult(intentFor<SongActivity>().putExtra("song_edit", song), 0)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<SongActivity>(0)
            R.id.item_logout -> doLogout()

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadSongs()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadSongs() {
        sortData(ascending)
        showSongs(app.songs.findAll() as ArrayList<SongModel>)
    }

    private fun sortData(asc: Boolean) {
        //SORT ARRAY ASCENDING AND DESCENDING
        if (!asc) {
            songs.reverse()
        }

        else {
        }
        recyclerView.adapter = SongAdapter(songs, this)
    }

    fun showSongs (songs: ArrayList<SongModel>) {
        recyclerView.adapter = SongAdapter(songs, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        app.songs.clear()
        startActivity(intentFor<LoginView>())

    }

}




