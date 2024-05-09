package com.kuldeep.pestopractical.ui.DashBoard

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.kuldeep.pestopractical.databinding.TaskListItemBinding
import com.kuldeep.pestopractical.ui.AddTask.TasksModels

class TaskRVAdapter(
    var taskList: ArrayList<TasksModels>,
    var deleteClickInterface: DeleteClickInterface
) : RecyclerView.Adapter<TaskRVAdapter.ViewHolder>(), Filterable {

    // use this in base class to access items
    protected var values = taskList

    class ViewHolder(val binding: TaskListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TaskListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    protected open fun searchCriteria(searchText: String, value: TasksModels): Boolean {
        return value.status.equals(searchText)
    }

    fun search(s: String) {
        filter.filter(s)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val taskModel = values[position]
            binding.tvTitle.text = taskModel.title
            binding.tvDesc.text = taskModel.desc
            binding.tvStatus.text = taskModel.status.toString()
            binding.tvEditStatus.text = taskModel.createStatus.toString()
            binding.tvDate.text = "Date: " + taskModel.date

            binding.deleteTask.setOnClickListener {
                deleteClickInterface.onDeleteBtnClickInterface(position)
            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    values = taskList
                } else {
                    values = java.util.ArrayList()
                    val searchResults = taskList.filter { searchCriteria(charSearch, it) }
                    values.addAll(searchResults)
                }
                return filterResults.also {
                    it.values = values
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // no need to use "results" filtered list provided by this method.
                values = results?.values as ArrayList<TasksModels>
                notifyDataSetChanged()
            }
        }
    }

    fun deleteTaskList(position: Int) {
        taskList.removeAt(position)
        notifyDataSetChanged()
    }

    fun gettaskObjectFromPosition(position: Int): TasksModels {
        return values.get(position)
    }
}

// creating a interface for on Delete click
interface DeleteClickInterface {
    fun onDeleteBtnClickInterface(position: Int)
}