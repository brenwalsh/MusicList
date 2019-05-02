package org.wit.musiclist.views.login

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast
import org.wit.musiclist.models.firebase.SongFireStore
import org.wit.musiclist.views.BasePresenter
import org.wit.musiclist.views.BaseView
import org.wit.musiclist.views.VIEW

class LoginPresenter(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: SongFireStore? = null

    init {
        if (app.songs is SongFireStore) {
            fireStore = app.songs as SongFireStore
        }
    }

    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchSongs {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST)
                    }
                } else {
                    view?.hideProgress()
                    view?.navigateTo(VIEW.LIST)
                }
            } else {
                view?.hideProgress()
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }
    }

    fun doSignUp(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                view?.hideProgress()
                view?.navigateTo(VIEW.LIST)
            } else {
                view?.hideProgress()
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }
    }
}