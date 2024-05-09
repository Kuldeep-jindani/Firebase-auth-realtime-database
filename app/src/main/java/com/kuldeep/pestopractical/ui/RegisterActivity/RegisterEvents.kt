package com.kuldeep.pestopractical.ui.RegisterActivity

sealed class RegisterEvents {

    class showToast(
        val message : String
    ) : RegisterEvents()
    object showLoading : RegisterEvents()
    object hideLoading : RegisterEvents()
    object startDashboardActivity : RegisterEvents()
    object onLoginClick : RegisterEvents()

}