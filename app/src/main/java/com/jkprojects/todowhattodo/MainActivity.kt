package com.jkprojects.todowhattodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.jkprojects.todowhattodo.data.TaskDatabase
import com.jkprojects.todowhattodo.data.UserPreferencesRepositoryImpl
import com.jkprojects.todowhattodo.ui.TaskScreen
import com.jkprojects.todowhattodo.ui.theme.ToDoWhatToDoTheme
import com.jkprojects.todowhattodo.viewmodel.TaskViewModel
import com.jkprojects.todowhattodo.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskDatabase.getDatabase(this).taskDao(),
            UserPreferencesRepositoryImpl(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoWhatToDoTheme {
                TaskScreen(
                    viewModel = taskViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
