package com.kuldeep.pestopractical.ui.AddTask

import androidx.lifecycle.ViewModel
import com.kuldeep.pestopractical.ui.LoginActivity.LoginEvents
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddTaskViewModel @Inject constructor() : ViewModel() {

    suspend fun showToastToViewmodel(
        message : String
    ) = flow {
        emit(AddTaskEvents.showToast(message))
        emit(AddTaskEvents.hideLoading)
    }
}