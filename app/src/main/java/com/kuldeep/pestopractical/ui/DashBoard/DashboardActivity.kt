package com.kuldeep.pestopractical.ui.DashBoard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuldeep.pestopractical.databinding.ActivityDashboardBinding
import com.kuldeep.pestopractical.ui.AddTask.AddTaskActivity
import com.kuldeep.pestopractical.ui.AddTask.TasksModels
import com.kuldeep.pestopractical.ui.LoginActivity.FireBaseViewModel
import com.kuldeep.pestopractical.ui.Model.CreateStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), DeleteClickInterface {

    private var _binding: ActivityDashboardBinding? = null
    private val binding get() = _binding!!

    private val dashBoardViewModel: DashBoardViewModel by viewModels()
    private val fireBaseViewModel: FireBaseViewModel by viewModels()

    private lateinit var taskAdapter: TaskRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initListeners()
    }

    private fun initListeners() {
        binding.dashboardAddNewTask.setOnClickListener {
            addTaskActivityClick()
        }

        binding.chipAll.setOnClickListener {
            dashBoardViewModel.chipSelected = "All"
        }
        binding.chipNew.setOnClickListener {
            filterInViewModel(binding.chipNew.text.toString())
            dashBoardViewModel.chipSelected = "New"
        }
        binding.chipInProcess.setOnClickListener {
            filterInViewModel(binding.chipInProcess.text.toString())
            dashBoardViewModel.chipSelected = "In Process"
        }
        binding.chipDone.setOnClickListener {
            filterInViewModel(binding.chipDone.text.toString())
            dashBoardViewModel.chipSelected = "Done"
        }
    }

    private fun initView() {
        binding.idPBLoading.visibility = View.VISIBLE
        with(binding.dashboardTasklist) {
            val layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            this.layoutManager = layoutManager
            getTasks()
            loadTaskListAdapter()
        }
    }

    private fun loadTaskListAdapter() {
        taskAdapter = TaskRVAdapter(
            taskList = dashBoardViewModel.taskList,
            this@DashboardActivity
        )
        binding.dashboardTasklist.adapter = taskAdapter
    }

    private fun getTasks() {
        // Read from the database
        fireBaseViewModel.getTaskList()
        fireBaseViewModel.taskListStatus.observeForever {
            dashBoardViewModel.taskList = it as ArrayList<TasksModels>
            loadTaskListAdapter()
        }
        hideLoading()
    }

    fun filterInViewModel(status: String) {
        lifecycleScope.launch {
            dashBoardViewModel.filterTaskList(status).collect {
                updateOnEvent(it)
            }
        }
    }

    override fun onDeleteBtnClickInterface(position: Int) {
        deleteTaskClick(position)
    }

    fun addTaskActivityClick() {
        lifecycleScope.launch {
            dashBoardViewModel.addTaskActivityClick().collect {
                updateOnEvent(it)
            }
        }
    }

    fun deleteTaskClick(position: Int) {
        val tasksModels = taskAdapter.gettaskObjectFromPosition(position)
        fireBaseViewModel.deleteTaskAtPosition(tasksModels.key!!, position)
        getTasks()
        lifecycleScope.launch {
            dashBoardViewModel.deleteTaskClick(position).collect {
                updateOnEvent(it)
            }
        }
    }

    fun hideLoading() {
        lifecycleScope.launch {
            dashBoardViewModel.hideLoading().collect {
                updateOnEvent(it)
            }
        }
    }

    private fun updateOnEvent(dashboardEvent: DashboardEvents) {
        when (dashboardEvent) {
            is DashboardEvents.addTaskClick -> {
                intent = Intent(this, AddTaskActivity::class.java).also {
                    it.putExtra("taskMode", CreateStatus.NEW)
                    startActivity(it)
                }
            }
            is DashboardEvents.deleteFromList -> {
                taskAdapter.deleteTaskList(dashboardEvent.position)
            }

            is DashboardEvents.hideLoading -> binding.idPBLoading.visibility = View.GONE
            is DashboardEvents.showLoading -> binding.idPBLoading.visibility = View.VISIBLE
            is DashboardEvents.filterTaskList -> {
                binding.dashboardTasklist.removeAllViews()
                taskAdapter.search(dashboardEvent.status)
            }
        }
    }
}