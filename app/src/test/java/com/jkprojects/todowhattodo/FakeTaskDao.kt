package com.jkprojects.todowhattodo

import com.jkprojects.todowhattodo.data.TaskDao
import com.jkprojects.todowhattodo.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeTaskDao : TaskDao {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    override fun getAllTasks(): Flow<List<Task>> = _tasks

    override suspend fun insertTask(task: Task) {
        _tasks.update { it + task }
    }

    override suspend fun updateTask(task: Task) {
        _tasks.update { tasks ->
            tasks.map { if (it.id == task.id) task else it }
        }
    }

    override suspend fun deleteTask(task: Task) {
        _tasks.update { tasks ->
            tasks.filter { it.id != task.id }
        }
    }

    override suspend fun deleteTaskById(taskId: String) {
        _tasks.update { tasks ->
            tasks.filter { it.id != taskId }
        }
    }

    override suspend fun toggleTaskCompletion(taskId: String) {
        _tasks.update { tasks ->
            tasks.map { if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it }
        }
    }
}
