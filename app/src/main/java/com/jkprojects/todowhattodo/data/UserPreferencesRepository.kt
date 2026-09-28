package com.jkprojects.todowhattodo.data

import com.jkprojects.todowhattodo.model.Priority
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val lastUsedPriority: Flow<Priority>
    suspend fun saveLastUsedPriority(priority: Priority)
}
