package com.kuldeep.pestopractical.ui.RegisterActivity

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterViewModel @Inject constructor() : ViewModel() {

    suspend fun showToastToViewmodel(
        message: String
    ) = flow {
        emit(RegisterEvents.showToast(message))
    }

    suspend fun afterLoginEvents(
    ) = flow {
        emit(RegisterEvents.showLoading)
        emit(RegisterEvents.hideLoading)
        emit(RegisterEvents.showToast(message = ("Registration Sucessful!!")))
        emit(RegisterEvents.startDashboardActivity)
    }

    suspend fun onRegisterBtnClick() = flow {
        emit(RegisterEvents.onLoginClick)
    }
}