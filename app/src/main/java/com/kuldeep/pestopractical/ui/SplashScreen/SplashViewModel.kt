package com.kuldeep.pestopractical.ui.SplashScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashViewModel @Inject constructor() : ViewModel() {

    suspend fun startLoginActivity() = flow{
        emit(SplashEvents.startLoginActivity)
    }

}