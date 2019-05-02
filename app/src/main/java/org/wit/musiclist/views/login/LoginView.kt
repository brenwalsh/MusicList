package org.wit.musiclist.views.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.wit.musiclist.R
import org.wit.musiclist.activities.SongListActivity
import org.wit.musiclist.views.BaseView

class LoginView : BaseView() {

    lateinit var presenter: LoginPresenter
    val RC_SIGN_IN = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        hideProgress()

        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

        configureGoogleSignIn()
        setupUI()
        firebaseAuth = FirebaseAuth.getInstance()

        signUp.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
                showButtons()
            }
            else {
                presenter.doSignUp(email,password)
            }
        }

        logIn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
                showButtons()
            }
            else {
                presenter.doLogin(email,password)
            }
        }
    }
    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
        signUp.visibility = View.INVISIBLE
        logIn.visibility = View.INVISIBLE
        google_button.visibility = View.INVISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    fun showButtons(){
        signUp.visibility = View.VISIBLE
        logIn.visibility = View.VISIBLE
        google_button.visibility = View.VISIBLE
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun setupUI() {
        google_button.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> =  GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

                startActivity(SongListActivity.getLaunchIntent(this))
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, LoginView::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

}