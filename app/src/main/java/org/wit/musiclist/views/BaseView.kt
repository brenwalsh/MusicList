package org.wit.musiclist.views

import android.content.Intent

import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import org.jetbrains.anko.AnkoLogger

import org.wit.musiclist.models.SongModel
import org.wit.musiclist.activities.SongActivity
import org.wit.musiclist.activities.SongListActivity
import org.wit.musiclist.views.login.LoginView

enum class VIEW {
    SONG, LIST, LOGIN
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, SongListActivity::class.java)
        when (view) {
            VIEW.SONG -> intent = Intent(this, SongActivity::class.java)
            VIEW.LIST -> intent = Intent(this, SongListActivity::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)

        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar) {
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showSong(song: SongModel) {}
    open fun showSongs(songs: List<SongModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}