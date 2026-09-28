package com.jkprojects.todowhattodo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkprojects.todowhattodo.data.TaskDao
import com.jkprojects.todowhattodo.data.UserPreferencesRepository
import com.jkprojects.todowhattodo.model.Filter
import com.jkprojects.todowhattodo.model.Priority
import com.jkprojects.todowhattodo.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskDao: TaskDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val rawTasks: StateFlow<List<Task>> = taskDao.getAllTasks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val selectedPriority: StateFlow<Priority> = userPreferencesRepository.lastUsedPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Priority.MEDIUM
    )

    private val _filter = MutableStateFlow(Filter.ALL)
    val filter: StateFlow<Filter> = _filter.asStateFlow()
    val filterState: StateFlow<Filter> = _filter.asStateFlow()

    val tasks: StateFlow<List<Task>> = combine(rawTasks, _filter) { taskList, currentFilter ->
        when (currentFilter) {
            Filter.ALL -> taskList
            Filter.ACTIVE -> taskList.filter { !it.isCompleted }
            Filter.COMPLETED -> taskList.filter { it.isCompleted }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    val taskList: StateFlow<List<Task>> = tasks

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }

    fun addTask(title: String, priority: Priority = Priority.MEDIUM) {
        val newTask = Task(title = title, priority = priority)
        addTask(newTask)
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskDao.deleteTaskById(taskId)
        }
    }

    fun deleteTask(task: Task) {
        deleteTask(task.id)
    }

    fun toggleTaskCompletion(taskId: String) {
        viewModelScope.launch {
            taskDao.toggleTaskCompletion(taskId)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        toggleTaskCompletion(task.id)
    }

    fun toggleCompletion(taskId: String) {
        toggleTaskCompletion(taskId)
    }

    fun toggleCompletion(task: Task) {
        toggleTaskCompletion(task)
    }

    fun setFilter(filter: Filter) {
        _filter.value = filter
    }

    fun setPriority(priority: Priority) {
        viewModelScope.launch {
            userPreferencesRepository.saveLastUsedPriority(priority)
        }
    }
}
