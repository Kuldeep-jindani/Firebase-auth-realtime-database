package com.kuldeep.pestopractical.ui.AddTask

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.kuldeep.pestopractical.R
import com.kuldeep.pestopractical.data.util.ResultOf
import com.kuldeep.pestopractical.databinding.ActivityAddTaskBinding
import com.kuldeep.pestopractical.ui.LoginActivity.FireBaseViewModel
import com.kuldeep.pestopractical.ui.Model.CreateStatus
import kotlinx.coroutines.launch
import java.util.Date


class AddTaskActivity : AppCompatActivity() {

    private var _binding: ActivityAddTaskBinding? = null
    private val binding get() = _binding!!

    private val addTaskViewModel: AddTaskViewModel by viewModels()
    private val fireBaseViewModel: FireBaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initListeners()
    }

    private fun initView() {
        if (intent.getStringExtra("taskMode")?.equals(CreateStatus.EDIT.toString()) == true) {
            val tasksModel = intent.extras as TasksModels
            binding.idEdtTaskTitle.setText(tasksModel!!.title)
            binding.idEdtTaskDesc.setText(tasksModel.desc)
        }
        // Create a list to display in the Spinner
        val mList = arrayOf<String?>("New", "In Process", "Done")
        // Create an adapter as shown below
        val mArrayAdapter = ArrayAdapter<Any?>(this, R.layout.spinner_list_item, mList)
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)

        // Set the adapter to the Spinner
        binding.spinStatus.adapter = mArrayAdapter
    }

    private fun initListeners() {
        binding.idBtnAddTask.setOnClickListener {
            val title = binding.idEdtTaskTitle.text.toString()
            val desc = binding.idEdtTaskDesc.text.toString()
            val status = binding.spinStatus.selectedItem.toString()
            if (title.isEmpty()) {
                binding.idEdtTaskTitle.error = "Title Should not be empty"
            } else if (desc.isEmpty()) {
                binding.idEdtTaskDesc.error = "Description Should not be empty"
            } else {
                addTaskToDB(
                    title = title,
                    desc = desc,
                    status = status
                )
            }
        }
    }

    private fun addTaskToDB(title: String, desc: String, status: String) {
        binding.idPBLoading.visibility = View.VISIBLE
        val d = Date()
        val dateStr: CharSequence = DateFormat.format("dd-MM-yyyy", d.getTime())
        /*if (intent.getStringExtra("taskMode") == TaskMode.EDIT.toString()) {
            taskHashMap.put("editstatus", CreateStatus.EDITED.toString())
        }else*/

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        val key: String = myRef.push().key.toString()
        val tasksModels = TasksModels(
            title = title,
            desc = desc,
            status = status,
            createStatus = intent.getStringExtra("taskMode"),
            date = dateStr.toString(),
            key = key
        )
        fireBaseViewModel.addTask(tasksModels)
        fireBaseViewModel.addTaskStatus.observe(this, Observer {
            when (it) {
                is ResultOf.Success -> {
                    if (it.value.equals("Tasks Added..!", ignoreCase = true)) {
                        // on below line we are hiding our progress bar.
                        showToastToViewmodel(it.value)
                    }
                }

                is ResultOf.Failure -> {
                    val failedMessage = it.message ?: "Unknown Error"
                    showToastToViewmodel("Login failed with $failedMessage")
                }
            }
        })
    }

    fun updateViewOnEvent(
        addTaskEvents: AddTaskEvents
    ) {
        when (addTaskEvents) {
            is AddTaskEvents.hideLoading -> binding.idPBLoading.visibility = View.GONE
            is AddTaskEvents.showLoading -> binding.idPBLoading.visibility = View.VISIBLE
            is AddTaskEvents.showToast -> {
                Toast.makeText(
                    this@AddTaskActivity,
                    addTaskEvents.message,
                    Toast.LENGTH_SHORT
                ).show()
                binding.idEdtTaskTitle.setText("")
                binding.idEdtTaskDesc.setText("")
            }
        }
    }

    fun showToastToViewmodel(
        message: String
    ) {
        lifecycleScope.launch {
            addTaskViewModel.showToastToViewmodel(message).collect {
                updateViewOnEvent(it)
            }
        }
    }
}