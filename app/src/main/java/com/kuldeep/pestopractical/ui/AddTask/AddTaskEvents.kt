package com.kuldeep.pestopractical.ui.AddTask

sealed class AddTaskEvents {

    class showToast(
        val message : String
    ) : AddTaskEvents()
    object showLoading : AddTaskEvents()
    object hideLoading : AddTaskEvents()
}