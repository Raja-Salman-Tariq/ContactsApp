package com.example.a4v4.utils

import android.app.Activity
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.a4v4.MainActivity
import com.example.a4v4.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInHelper(val mainActivity: MainActivity) {

    private var user    :   FirebaseUser?    =   null

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var firebaseAuth: FirebaseAuth? = null


    // Call back to start new (account picker) activity and to handle result
    private val resultContract  =   mainActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleSignUpResult(task)
        }
    }

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        user    =   firebaseAuth?.currentUser
    }

    // configures sign in to be done according to Google Sign In
    private fun signIn(){
        if (user    ==  null) {

            val googleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(mainActivity.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            mGoogleSignInClient = GoogleSignIn.getClient(mainActivity, googleSignInOptions)
            mGoogleSignInClient?.signOut()
            val signIn = mGoogleSignInClient?.signInIntent
            resultContract.launch(signIn)
        }
        else user = firebaseAuth?.currentUser
    }

    // if account successfuly selected, try to sign in
    private fun handleSignUpResult(myCompletedTask: Task<GoogleSignInAccount>) {
        try {
            val account = myCompletedTask.getResult(ApiException::class.java)
            firebaseGoogleAuth(account)
        } catch (e: ApiException) {
            firebaseGoogleAuth(null)
        }
    }

    // process the sign in request. Also where the backed up data uploading should be handled
    private fun firebaseGoogleAuth(myAccount: GoogleSignInAccount?) {
        //check if account is null
        if (myAccount != null) {
            val myAuthCredential = GoogleAuthProvider.getCredential(myAccount.idToken, null)
            firebaseAuth!!.signInWithCredential(myAuthCredential).addOnCompleteListener(mainActivity
            ) { task ->
                if (task.isSuccessful) {
                    Snackbar.make(mainActivity.binding.root, "Saved To Drive.", LENGTH_INDEFINITE)
                        .setAction("OK"){}.show()
                    user    =   firebaseAuth?.currentUser
                }
            }
        }
        user    =   firebaseAuth?.currentUser
    }

    // signin and backup app data; exposed function
    fun backUp() {
        signIn()
    }

    // log out the user; exposed function
    fun logOut(){
        if (user!=null) {
            user = null
            mGoogleSignInClient?.signOut()
            firebaseAuth?.signOut()
            Snackbar.make(mainActivity.binding.root, "Logged Out.", LENGTH_INDEFINITE)
                .setAction("OK"){}.show()
        }
        else{
            Snackbar.make(mainActivity.binding.root, "Not logged in yet. \n" +
                    "Click on \"Backup To Drive\" from the options menu to log in.",
                LENGTH_INDEFINITE).apply {
                setAction("OK") {}
                view
                    .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    .maxLines = 3
                show()
            }
        }
    }
}