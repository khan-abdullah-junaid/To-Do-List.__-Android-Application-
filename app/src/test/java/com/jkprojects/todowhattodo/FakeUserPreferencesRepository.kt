package com.jkprojects.todowhattodo

import com.jkprojects.todowhattodo.data.UserPreferencesRepository
import com.jkprojects.todowhattodo.model.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserPreferencesRepository : UserPreferencesRepository {
    private val _priority = MutableStateFlow(Priority.MEDIUM)
    override val lastUsedPriority: Flow<Priority> = _priority

    override suspend fun saveLastUsedPriority(priority: Priority) {
        _priority.value = priority
    }
}
