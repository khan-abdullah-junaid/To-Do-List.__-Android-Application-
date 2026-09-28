package com.jkprojects.todowhattodo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val priority: Priority = Priority.MEDIUM,
    val isCompleted: Boolean = false,
)
