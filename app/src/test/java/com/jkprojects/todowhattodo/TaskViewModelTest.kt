package com.jkprojects.todowhattodo

import com.jkprojects.todowhattodo.model.Filter
import com.jkprojects.todowhattodo.model.Priority
import com.jkprojects.todowhattodo.model.Task
import com.jkprojects.todowhattodo.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TaskViewModel
    private lateinit var fakeTaskDao: FakeTaskDao
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTaskDao = FakeTaskDao()
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        viewModel = TaskViewModel(fakeTaskDao, fakeUserPreferencesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        val job = launch(UnconfinedTestDispatcher()) { viewModel.rawTasks.collect {} }
        advanceUntilIdle()
        assertEquals(emptyList<Task>(), viewModel.rawTasks.value)
        assertEquals(Filter.ALL, viewModel.filter.value)
        job.cancel()
    }

    @Test
    fun testAddTask() = runTest {
        val job = launch(UnconfinedTestDispatcher()) { viewModel.rawTasks.collect {} }
        viewModel.addTask("Buy Groceries", Priority.HIGH)
        advanceUntilIdle()
        assertEquals(1, viewModel.rawTasks.value.size)

        val task = viewModel.rawTasks.value.first()
        assertEquals("Buy Groceries", task.title)
        assertEquals(Priority.HIGH, task.priority)
        assertFalse(task.isCompleted)
        job.cancel()
    }

    @Test
    fun testDeleteTask() = runTest {
        val job = launch(UnconfinedTestDispatcher()) { viewModel.rawTasks.collect {} }
        val task = Task(title = "Task to Delete")
        viewModel.addTask(task)
        advanceUntilIdle()
        assertEquals(1, viewModel.rawTasks.value.size)

        viewModel.deleteTask(task.id)
        advanceUntilIdle()
        assertTrue(viewModel.rawTasks.value.isEmpty())
        job.cancel()
    }

    @Test
    fun testToggleCompletion() = runTest {
        val job = launch(UnconfinedTestDispatcher()) { viewModel.rawTasks.collect {} }
        val task = Task(title = "Task to Complete")
        viewModel.addTask(task)
        advanceUntilIdle()

        assertFalse(viewModel.rawTasks.value.first().isCompleted)

        viewModel.toggleTaskCompletion(task.id)
        advanceUntilIdle()
        assertTrue(viewModel.rawTasks.value.first().isCompleted)

        viewModel.toggleTaskCompletion(task.id)
        advanceUntilIdle()
        assertFalse(viewModel.rawTasks.value.first().isCompleted)
        job.cancel()
    }

    @Test
    fun testSetFilter() = runTest {
        val job = launch(UnconfinedTestDispatcher()) { viewModel.rawTasks.collect {} }
        viewModel.addTask("Active Task", Priority.LOW)
        val completedTask = Task(title = "Completed Task", isCompleted = true)
        viewModel.addTask(completedTask)
        advanceUntilIdle()

        viewModel.setFilter(Filter.ALL)
        assertEquals(Filter.ALL, viewModel.filter.value)

        viewModel.setFilter(Filter.ACTIVE)
        assertEquals(Filter.ACTIVE, viewModel.filter.value)

        viewModel.setFilter(Filter.COMPLETED)
        assertEquals(Filter.COMPLETED, viewModel.filter.value)
        job.cancel()
    }
}
