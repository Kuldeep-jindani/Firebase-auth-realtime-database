package com.kuldeep.pestopractical.ui.DashBoard

import com.kuldeep.pestopractical.ui.AddTask.TasksModels

sealed class DashboardEvents {

    class deleteFromList(
        val position: Int
    ) : DashboardEvents()
    object addTaskClick : DashboardEvents()
    object hideLoading : DashboardEvents()
    object showLoading : DashboardEvents()
    class filterTaskList(
        val status : String
    ) : DashboardEvents()
}