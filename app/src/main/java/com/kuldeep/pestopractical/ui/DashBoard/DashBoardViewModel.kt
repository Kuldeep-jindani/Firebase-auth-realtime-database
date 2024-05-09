package com.kuldeep.pestopractical.ui.DashBoard

import androidx.lifecycle.ViewModel
import com.kuldeep.pestopractical.ui.AddTask.TasksModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor() : ViewModel() {

    var newTaskList = ArrayList<TasksModels>()
    var inProcessTaskList = ArrayList<TasksModels>()
    var doneTaskList = ArrayList<TasksModels>()
    var allTaskList = ArrayList<TasksModels>()

    var chipSelected: String = "All"

    var taskList = ArrayList<TasksModels>()
    var finaltaskList = ArrayList<TasksModels>()

    fun addTaskActivityClick() = flow {
        emit(DashboardEvents.addTaskClick)
    }

    fun deleteTaskClick(position: Int) = flow {
        emit(DashboardEvents.deleteFromList(position))
    }

    fun hideLoading() = flow {
        emit(DashboardEvents.hideLoading)
    }

   /* fun filterByStatus() {
        newTaskList = taskList.filter { it.status.equals("New") } as ArrayList<TasksModels>
        inProcessTaskList =
            taskList.filter { it.status.equals("In Process") } as ArrayList<TasksModels>
        doneTaskList = taskList.filter { it.status.equals("Done") } as ArrayList<TasksModels>
        allTaskList = taskList
    }*/

   /* fun setListInAdapter() = flow {
        finaltaskList = if (chipSelected.equals("New")) {
            newTaskList
        } else if (chipSelected.equals("In Process")) {
            inProcessTaskList
        } else if (chipSelected.equals("Done")) {
            doneTaskList
        } else {
            allTaskList
        }
        emit(DashboardEvents.filterTaskList(finaltaskList))
    }*/

    fun filterTaskList(status: String) = flow {
        /*val filteredList: ArrayList<TasksModels> = ArrayList()
        val tempList: ArrayList<TasksModels> = taskList
        for (i in 0 until tempList.size) {
            val text: String = tempList.get(i).status.toString()
            if (text.equals(status)) {
                filteredList.add(tempList.get(i))
            }
        }*/
        /*val tempList = if (status.equals("New")) {
            newTaskList
        } else if (status.equals("In Process")) {
            inProcessTaskList
        } else if (status.equals("Done")) {
            doneTaskList
        } else {
            allTaskList
        }*/
        emit(DashboardEvents.filterTaskList(status))
    }

    /*fun withoutFilterInViewModel() = flow {
        emit(DashboardEvents.filterTaskList(allTaskList))
    }*/
}