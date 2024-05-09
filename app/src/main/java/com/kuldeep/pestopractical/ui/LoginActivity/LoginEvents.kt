package com.kuldeep.pestopractical.ui.LoginActivity

import com.google.firebase.auth.FirebaseUser

sealed class LoginEvents {

    class showToast(
       val message : String
    ) : LoginEvents()

    class currentUser(
       val user : FirebaseUser?
    ) : LoginEvents()
    object showLoading : LoginEvents()
    object hideLoading : LoginEvents()
    object startLoginActivity : LoginEvents()
    object onRegisterClick : LoginEvents()
}