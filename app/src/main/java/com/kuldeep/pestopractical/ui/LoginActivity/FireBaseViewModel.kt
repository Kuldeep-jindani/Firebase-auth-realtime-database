package com.kuldeep.pestopractical.ui.LoginActivity

import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kuldeep.pestopractical.data.util.ResultOf
import com.kuldeep.pestopractical.ui.AddTask.TasksModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.annotation.Nullable
import javax.inject.Inject

@HiltViewModel
class FireBaseViewModel @Inject constructor() : ViewModel() {

    private val LOG_TAG = "FireBaseViewModel"
    private var auth: FirebaseAuth? = null
    private var rootNode: FirebaseDatabase
    private var reference: DatabaseReference
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {

        auth = FirebaseAuth.getInstance()
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode.getReference("message")
        loading.postValue(false)
    }

    private val _addTaskStatus = MutableLiveData<ResultOf<String>>()
    val addTaskStatus: LiveData<ResultOf<String>> = _addTaskStatus
    fun addTask(
        tasksModels: TasksModels
    ) {
        tasksModels.key?.let {
            reference.child(it).setValue(tasksModels).addOnCompleteListener { tasks ->
                _addTaskStatus.postValue(ResultOf.Success("Tasks Added..!"))
            }
        }
    }

    private val _taskListStatus = MutableLiveData<List<TasksModels>>()
    val taskListStatus: LiveData<List<TasksModels>> = _taskListStatus
    fun getTaskList() {

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val taskList: List<TasksModels> = snapshot.children.map {
                    it.getValue(TasksModels::class.java)!!
                }
                Log.e("Firebase Tasks List",taskList.toString())
                _taskListStatus.postValue(taskList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    val _deleteStatus = MutableLiveData<ResultOf<String>>()
    val deleteStatus : LiveData<ResultOf<String>> = _deleteStatus
    fun deleteTaskAtPosition(key : String, position : Int){
        val deleteRef =reference.child(key)
        deleteRef.removeValue().addOnSuccessListener {
            _deleteStatus.postValue(ResultOf.Success("Item Removed"))
            getTaskList()
        }
    }

    private val _currentUserStatus = MutableLiveData<ResultOf<FirebaseUser>>()
    val currentUserStatus: LiveData<ResultOf<FirebaseUser>> = _currentUserStatus
    fun currentUser() {
        val currentUser = auth?.currentUser
        if (currentUser != null) {
            _currentUserStatus.postValue(ResultOf.Success(auth?.currentUser!!))
        }

    }

    private val _registrationStatus = MutableLiveData<ResultOf<String>>()
    val registrationStatus: LiveData<ResultOf<String>> = _registrationStatus
    fun signUp(email: String, password: String) {
        loading.postValue(true)
        viewModelScope.launch {
            var errorCode = -1
            try {
                auth?.let { authentication ->
                    authentication.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (!task.isSuccessful) {
                                println("Registration Failed with ${task.exception}")
                                _registrationStatus.postValue(
                                    ResultOf.Failure(
                                        "Registration Failed with ${task.exception}",
                                        task.exception
                                    )
                                )
                            } else {
                                _registrationStatus.postValue(ResultOf.Success("UserCreated"))

                            }
                            loading.postValue(false)
                        }

                }
            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code ${errorCode} ",
                            e
                        )
                    )
                } else {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            e
                        )
                    )
                }


            }
        }
    }

    private val _signInStatus = MutableLiveData<ResultOf<String>>()
    val signInStatus: LiveData<ResultOf<String>> = _signInStatus
    fun signIn(email: String, password: String) {
        loading.postValue(true)
        viewModelScope.launch {
            var errorCode = -1
            try {
                auth?.let { login ->
                    login.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->

                            if (!task.isSuccessful) {
                                println("Login Failed with ${task.exception}")
                                _signInStatus.postValue(ResultOf.Success("Login Failed with ${task.exception}"))
                            } else {
                                _signInStatus.postValue(ResultOf.Success("Login Successful"))

                            }
                            loading.postValue(false)
                        }

                }

            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code ${errorCode} ",
                            e
                        )
                    )
                } else {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            e
                        )
                    )
                }


            }
        }
    }

    fun resetSignInLiveData() {
        _signInStatus.value = ResultOf.Success("Reset")
    }

    private val _signOutStatus = MutableLiveData<ResultOf<String>>()
    val signOutStatus: LiveData<ResultOf<String>> = _signOutStatus
    fun signOut() {
        loading.postValue(true)
        viewModelScope.launch {
            var errorCode = -1
            try {
                auth?.let { authentation ->
                    authentation.signOut()
                    _signOutStatus.postValue(ResultOf.Success("Signout Successful"))
                    loading.postValue(false)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _signOutStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code ${errorCode} ",
                            e
                        )
                    )
                } else {
                    _signOutStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            e
                        )
                    )
                }


            }
        }
    }

    fun fetchLoading(): LiveData<Boolean> = loading


    /*
    Registration Failed with com.google.firebase.FirebaseException: An internal error has occurred. [ Unable to resolve host "www.googleapis.com":No address associated with hostname ]
     */
}