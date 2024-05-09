package com.kuldeep.pestopractical.ui.LoginActivity

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()

    suspend fun afterLoginEvents(
    ) = flow {
        emit(LoginEvents.showLoading)
        emit(LoginEvents.hideLoading)
        emit(LoginEvents.showToast(message = ("Login Sucessful!!")))
        emit(LoginEvents.startLoginActivity)
    }

    suspend fun showToastToViewmodel(
        message : String
    ) = flow {
        emit(LoginEvents.showToast(message))
    }

    suspend fun firebaseAuthOnStart() = flow {
        val user = mAuth.currentUser
        emit(LoginEvents.currentUser(user))
    }

    suspend fun onRegisterBtnClick() = flow {
        emit(LoginEvents.onRegisterClick)
    }
}