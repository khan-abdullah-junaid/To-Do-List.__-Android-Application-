package com.jkprojects.todowhattodo.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jkprojects.todowhattodo.model.Filter
import com.jkprojects.todowhattodo.model.Priority
import com.jkprojects.todowhattodo.model.Task
import com.jkprojects.todowhattodo.ui.theme.PriorityColorSet
import com.jkprojects.todowhattodo.ui.theme.ToDoWhatToDoTheme
import com.jkprojects.todowhattodo.ui.theme.getPriorityColorSet
import com.jkprojects.todowhattodo.viewmodel.TaskViewModel
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val rawTasks by viewModel.rawTasks.collectAsStateWithLifecycle()
    val currentFilter by viewModel.filter.collectAsStateWithLifecycle()
    val selectedPriority by viewModel.selectedPriority.collectAsStateWithLifecycle()

    TaskScreenContent(
        tasks = tasks,
        allTasks = rawTasks,
        currentFilter = currentFilter,
        selectedPriority = selectedPriority,
        onAddTask = { title, priority ->
            viewModel.addTask(title, priority)
        },
        onPrioritySelected = { priority ->
            viewModel.setPriority(priority)
        },
        onToggleTaskCompletion = { taskId ->
            viewModel.toggleTaskCompletion(taskId)
        },
        onDeleteTask = { taskId ->
            viewModel.deleteTask(taskId)
        },
        onSetFilter = { filter ->
            viewModel.setFilter(filter)
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenContent(
    tasks: List<Task>,
    allTasks: List<Task>,
    currentFilter: Filter,
    selectedPriority: Priority,
    onAddTask: (String, Priority) -> Unit,
    onPrioritySelected: (Priority) -> Unit,
    onToggleTaskCompletion: (String) -> Unit,
    onDeleteTask: (String) -> Unit,
    onSetFilter: (Filter) -> Unit,
    modifier: Modifier = Modifier
) {
    var taskTitle by remember { mutableStateOf("") }

    val handleAddTask = {
        if (taskTitle.isNotBlank()) {
            onAddTask(taskTitle.trim(), selectedPriority)
            taskTitle = ""
        }
    }

    val completedCount = allTasks.count { it.isCompleted }
    val activeCount = allTasks.size - completedCount

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "To-Do List",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Stats & Progress Summary Card
            ProgressSummaryCard(
                totalCount = allTasks.size,
                completedCount = completedCount,
                activeCount = activeCount
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Task Creation Input Bar
            TaskInputSection(
                taskTitle = taskTitle,
                onTitleChange = { taskTitle = it },
                selectedPriority = selectedPriority,
                onPrioritySelected = onPrioritySelected,
                onAddTask = handleAddTask
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Chips Bar with Counters
            FilterChipsRow(
                currentFilter = currentFilter,
                allCount = allTasks.size,
                activeCount = activeCount,
                completedCount = completedCount,
                onFilterSelected = onSetFilter
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Task List or Empty State
            if (tasks.isEmpty()) {
                EmptyStateView(
                    filter = currentFilter,
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = tasks,
                        key = { it.id }
                    ) { task ->
                        TaskItemRow(
                            task = task,
                            onToggleCompletion = { onToggleTaskCompletion(task.id) },
                            onDelete = { onDeleteTask(task.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressSummaryCard(
    totalCount: Int,
    completedCount: Int,
    activeCount: Int
) {
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 400),
        label = "progressAnimation"
    )

    val isDark = isSystemInDarkTheme()

    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "List Progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = when {
                            totalCount == 0 -> "No tasks created yet"
                            completedCount == totalCount -> "All tasks completed! 🎉"
                            else -> "$activeCount remaining task${if (activeCount == 1) "" else "s"}"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatBadge(
                    label = "Total",
                    count = totalCount,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
                StatBadge(
                    label = "Active",
                    count = activeCount,
                    containerColor = getPriorityColorSet(Priority.LOW, isDark).container,
                    contentColor = getPriorityColorSet(Priority.LOW, isDark).onContainer
                )
                StatBadge(
                    label = "Completed",
                    count = completedCount,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun StatBadge(
    label: String,
    count: Int,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        modifier = Modifier.padding(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun TaskInputSection(
    taskTitle: String,
    onTitleChange: (String) -> Unit,
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    onAddTask: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = onTitleChange,
                    placeholder = {
                        Text(
                            "What needs to be done?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onAddTask() }),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = onAddTask,
                    enabled = taskTitle.isNotBlank(),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                    ),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add task",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Priority Selector Row with colored badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Priority:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Priority.entries.forEach { priority ->
                    val isSelected = priority == selectedPriority
                    val isDark = isSystemInDarkTheme()
                    val priorityColorSet = getPriorityColorSet(priority, isDark)

                    val chipContainer = if (isSelected) priorityColorSet.container else MaterialTheme.colorScheme.surfaceContainerHigh
                    val chipContent = if (isSelected) priorityColorSet.onContainer else MaterialTheme.colorScheme.onSurfaceVariant

                    FilterChip(
                        selected = isSelected,
                        onClick = { onPrioritySelected(priority) },
                        label = {
                            Text(
                                text = priority.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(priorityColorSet.accent)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = chipContainer,
                            labelColor = chipContent,
                            selectedContainerColor = chipContainer,
                            selectedLabelColor = chipContent,
                            selectedLeadingIconColor = chipContent
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) priorityColorSet.border else Color.Transparent,
                            selectedBorderColor = priorityColorSet.border
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChipsRow(
    currentFilter: Filter,
    allCount: Int,
    activeCount: Int,
    completedCount: Int,
    onFilterSelected: (Filter) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Filter.entries.forEach { filter ->
            val isSelected = filter == currentFilter
            val (label, count) = when (filter) {
                Filter.ALL -> "All" to allCount
                Filter.ACTIVE -> "Active" to activeCount
                Filter.COMPLETED -> "Completed" to completedCount
            }

            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHighest
                            }
                        ) {
                            Text(
                                text = count.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
private fun TaskItemRow(
    task: Task,
    onToggleCompletion: () -> Unit,
    onDelete: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val priorityColorSet = getPriorityColorSet(task.priority, isDark)

    val cardBackgroundColor by animateColorAsState(
        targetValue = if (task.isCompleted) {
            MaterialTheme.colorScheme.surfaceContainerLowest
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 250),
        label = "cardBackgroundColor"
    )

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = cardBackgroundColor
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (task.isCompleted) 1.dp else 3.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (task.isCompleted) 0.dp else 1.dp,
                color = if (task.isCompleted) Color.Transparent else priorityColorSet.border.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // Left priority color bar / check box
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleCompletion() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = priorityColorSet.accent,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Task title and priority tag
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.isCompleted) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    ),
                    fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
                    color = if (task.isCompleted) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.isCompleted) {
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Priority Badge
            PriorityBadge(
                priority = task.priority,
                priorityColorSet = priorityColorSet,
                isCompleted = task.isCompleted
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Delete Button
            IconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete task",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun PriorityBadge(
    priority: Priority,
    priorityColorSet: PriorityColorSet,
    isCompleted: Boolean
) {
    Surface(
        color = if (isCompleted) {
            MaterialTheme.colorScheme.surfaceContainerHigh
        } else {
            priorityColorSet.container
        },
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = null,
                tint = if (isCompleted) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                } else {
                    priorityColorSet.accent
                },
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = priority.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                } else {
                    priorityColorSet.onContainer
                }
            )
        }
    }
}

@Composable
private fun EmptyStateView(
    filter: Filter,
    modifier: Modifier = Modifier
) {
    val (message, subtitle, icon) = when (filter) {
        Filter.ALL -> Triple(
            "No tasks yet!",
            "Type a task above and tap + to add it to your list.",
            Icons.Outlined.TaskAlt
        )
        Filter.ACTIVE -> Triple(
            "No active tasks!",
            "You are all caught up on your to-dos.",
            Icons.Outlined.CheckCircle
        )
        Filter.COMPLETED -> Triple(
            "No completed tasks yet",
            "Check off tasks above as you finish them.",
            Icons.Outlined.TaskAlt
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun TaskScreenLightPreview() {
    ToDoWhatToDoTheme {
        TaskScreenContent(
            tasks = listOf(
                Task(id = "1", title = "Submit high priority report to team", priority = Priority.HIGH, isCompleted = false),
                Task(id = "2", title = "Refine Material 3 theme styling and assets", priority = Priority.MEDIUM, isCompleted = true),
                Task(id = "3", title = "Read latest Jetpack Compose guidelines", priority = Priority.LOW, isCompleted = false)
            ),
            allTasks = listOf(
                Task(id = "1", title = "Submit high priority report to team", priority = Priority.HIGH, isCompleted = false),
                Task(id = "2", title = "Refine Material 3 theme styling and assets", priority = Priority.MEDIUM, isCompleted = true),
                Task(id = "3", title = "Read latest Jetpack Compose guidelines", priority = Priority.LOW, isCompleted = false)
            ),
            currentFilter = Filter.ALL,
            selectedPriority = Priority.MEDIUM,
            onAddTask = { _, _ -> },
            onPrioritySelected = {},
            onToggleTaskCompletion = {},
            onDeleteTask = {},
            onSetFilter = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TaskScreenDarkPreview() {
    ToDoWhatToDoTheme {
        TaskScreenContent(
            tasks = listOf(
                Task(id = "1", title = "Submit high priority report to team", priority = Priority.HIGH, isCompleted = false),
                Task(id = "2", title = "Refine Material 3 theme styling and assets", priority = Priority.MEDIUM, isCompleted = true),
                Task(id = "3", title = "Read latest Jetpack Compose guidelines", priority = Priority.LOW, isCompleted = false)
            ),
            allTasks = listOf(
                Task(id = "1", title = "Submit high priority report to team", priority = Priority.HIGH, isCompleted = false),
                Task(id = "2", title = "Refine Material 3 theme styling and assets", priority = Priority.MEDIUM, isCompleted = true),
                Task(id = "3", title = "Read latest Jetpack Compose guidelines", priority = Priority.LOW, isCompleted = false)
            ),
            currentFilter = Filter.ALL,
            selectedPriority = Priority.MEDIUM,
            onAddTask = { _, _ -> },
            onPrioritySelected = {},
            onToggleTaskCompletion = {},
            onDeleteTask = {},
            onSetFilter = {}
        )
    }
}
