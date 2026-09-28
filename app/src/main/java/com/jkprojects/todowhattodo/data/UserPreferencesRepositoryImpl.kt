package com.jkprojects.todowhattodo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jkprojects.todowhattodo.model.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepositoryImpl(private val context: Context) : UserPreferencesRepository {
    companion object {
        private val LAST_USED_PRIORITY = stringPreferencesKey("last_used_priority")
    }

    override val lastUsedPriority: Flow<Priority> = context.dataStore.data.map { preferences ->
        val priorityString = preferences[LAST_USED_PRIORITY] ?: Priority.MEDIUM.name
        try {
            Priority.valueOf(priorityString)
        } catch (e: Exception) {
            Priority.MEDIUM
        }
    }

    override suspend fun saveLastUsedPriority(priority: Priority) {
        context.dataStore.edit { preferences ->
            preferences[LAST_USED_PRIORITY] = priority.name
        }
    }
}
