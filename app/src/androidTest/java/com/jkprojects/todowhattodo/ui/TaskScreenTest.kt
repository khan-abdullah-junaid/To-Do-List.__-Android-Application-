package com.jkprojects.todowhattodo.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jkprojects.todowhattodo.model.Filter
import com.jkprojects.todowhattodo.model.Priority
import com.jkprojects.todowhattodo.model.Task
import com.jkprojects.todowhattodo.ui.theme.ToDoWhatToDoTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun taskScreenContent_emptyStateAllFilter_showsCorrectText() {
        composeTestRule.setContent {
            ToDoWhatToDoTheme {
                TaskScreenContent(
                    tasks = emptyList(),
                    allTasks = emptyList(),
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

        // Verify empty state text
        composeTestRule.onNodeWithText("No tasks yet!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type a task above and tap + to add it to your list.").assertIsDisplayed()
    }

    @Test
    fun taskScreenContent_displaysTasks() {
        val sampleTasks = listOf(
            Task(id = "1", title = "Buy groceries", priority = Priority.MEDIUM, isCompleted = false),
            Task(id = "2", title = "Pay bills", priority = Priority.HIGH, isCompleted = true)
        )

        composeTestRule.setContent {
            ToDoWhatToDoTheme {
                TaskScreenContent(
                    tasks = sampleTasks,
                    allTasks = sampleTasks,
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

        // Verify task titles are displayed
        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pay bills").assertIsDisplayed()
        // Completed text is displayed for the second task
        composeTestRule.onNodeWithText("Completed").assertIsDisplayed()
    }

    @Test
    fun taskScreenContent_addTask_invokesCallback() {
        var addedTitle = ""
        var addedPriority = Priority.LOW

        composeTestRule.setContent {
            ToDoWhatToDoTheme {
                TaskScreenContent(
                    tasks = emptyList(),
                    allTasks = emptyList(),
                    currentFilter = Filter.ALL,
                    selectedPriority = Priority.HIGH,
                    onAddTask = { title, priority ->
                        addedTitle = title
                        addedPriority = priority
                    },
                    onPrioritySelected = {},
                    onToggleTaskCompletion = {},
                    onDeleteTask = {},
                    onSetFilter = {}
                )
            }
        }

        // Enter task title
        composeTestRule.onNodeWithText("What needs to be done?")
            .performTextInput("New test task")
        
        // Select 'High' priority (already selected by state, but clicking triggers onPrioritySelected)
        // Wait, onAddTask uses selectedPriority state passed from top.

        // Click Add button
        composeTestRule.onNodeWithContentDescription("Add task").performClick()

        // Verify callback was invoked with correct data
        assertEquals("New test task", addedTitle)
        assertEquals(Priority.HIGH, addedPriority)
    }

    @Test
    fun taskScreenContent_toggleTaskCompletion_invokesCallback() {
        var toggledTaskId = ""
        val sampleTasks = listOf(
            Task(id = "task_id_1", title = "Task to complete", priority = Priority.LOW, isCompleted = false)
        )

        composeTestRule.setContent {
            ToDoWhatToDoTheme {
                TaskScreenContent(
                    tasks = sampleTasks,
                    allTasks = sampleTasks,
                    currentFilter = Filter.ALL,
                    selectedPriority = Priority.MEDIUM,
                    onAddTask = { _, _ -> },
                    onPrioritySelected = {},
                    onToggleTaskCompletion = { id -> toggledTaskId = id },
                    onDeleteTask = {},
                    onSetFilter = {}
                )
            }
        }

        // Checkbox usually doesn't have text, but we can find it as a toggleable node 
        // sibling to the task title, or find by semantic properties
        composeTestRule.onNodeWithText("Task to complete")
            .onSiblings()
            .filterToOne(isToggleable())
            .performClick()

        assertEquals("task_id_1", toggledTaskId)
    }

    @Test
    fun taskScreenContent_deleteTask_invokesCallback() {
        var deletedTaskId = ""
        val sampleTasks = listOf(
            Task(id = "task_id_2", title = "Task to delete", priority = Priority.MEDIUM, isCompleted = false)
        )

        composeTestRule.setContent {
            ToDoWhatToDoTheme {
                TaskScreenContent(
                    tasks = sampleTasks,
                    allTasks = sampleTasks,
                    currentFilter = Filter.ALL,
                    selectedPriority = Priority.MEDIUM,
                    onAddTask = { _, _ -> },
                    onPrioritySelected = {},
                    onToggleTaskCompletion = {},
                    onDeleteTask = { id -> deletedTaskId = id },
                    onSetFilter = {}
                )
            }
        }

        // Click the delete button
        composeTestRule.onNodeWithContentDescription("Delete task").performClick()

        assertEquals("task_id_2", deletedTaskId)
    }

    @Test
    fun taskScreenContent_setFilter_invokesCallback() {
        var selectedFilter: Filter? = null

        composeTestRule.setContent {
            ToDoWhatToDoTheme {
                TaskScreenContent(
                    tasks = emptyList(),
                    allTasks = emptyList(),
                    currentFilter = Filter.ALL,
                    selectedPriority = Priority.MEDIUM,
                    onAddTask = { _, _ -> },
                    onPrioritySelected = {},
                    onToggleTaskCompletion = {},
                    onDeleteTask = {},
                    onSetFilter = { filter -> selectedFilter = filter }
                )
            }
        }

        // The filter chips are labeled "All", "Active", "Completed"
        // Since we display counts like "Active 0" next to it, we need to match the text.
        // Or find the node with text "Active" and perform click.
        composeTestRule.onNodeWithText("Active").performClick()
        assertEquals(Filter.ACTIVE, selectedFilter)

        composeTestRule.onNodeWithText("Completed").performClick()
        assertEquals(Filter.COMPLETED, selectedFilter)
    }
}
