package com.kuldeep.pestopractical.ui.SplashScreen

import com.kuldeep.pestopractical.ui.RegisterActivity.RegisterEvents

sealed class SplashEvents {

    class showToast(
        val message : String
    ) : SplashEvents()
    object startLoginActivity : SplashEvents()
}