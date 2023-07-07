package com.example.todolist

import android.app.Application
import android.content.Context
import com.example.todolist.data.TodoItemsRepositoryImpl
import com.example.todolist.domain.AddTodoItemUseCase
import com.example.todolist.domain.DeleteTodoItemUseCase
import com.example.todolist.domain.EditTodoItemUseCase
import com.example.todolist.domain.GetTodoItemUseCase
import com.example.todolist.domain.GetTodoItemsFlowUseCase
import com.example.todolist.domain.TodoItemsRepository
import com.example.todolist.presentation.todolist.TodoListViewModelFactory
import com.example.todolist.presentation.todoitem.TodoItemViewModelFactory
import kotlinx.coroutines.withContext

class TodoListApplication : Application() {

    private val todoItemsRepository: TodoItemsRepository by lazy {
        TodoItemsRepositoryImpl(context = applicationContext)
    }

    private fun createGetTodoItemFlowUseCase() = GetTodoItemUseCase(
        todoItemsRepository,
    )

    private fun createGetTodoItemsFlowUseCase() = GetTodoItemsFlowUseCase(
        todoItemsRepository = todoItemsRepository,
    )

    private fun createAddTodoItemUseCase() = AddTodoItemUseCase(
        todoItemsRepository = todoItemsRepository,
    )

    private fun createDeleteTodoItemUseCase() = DeleteTodoItemUseCase(
        todoItemsRepository = todoItemsRepository,
    )

    private fun createEditTodoItemUseCase() = EditTodoItemUseCase(
        todoItemsRepository = todoItemsRepository,
    )

    val todoListItemViewModelFactory by lazy {
        TodoListViewModelFactory(
            getTodoItemsFlowUseCase = createGetTodoItemsFlowUseCase(),
            deleteTodoItemUseCase = createDeleteTodoItemUseCase(),
            editTodoItemUseCase = createEditTodoItemUseCase(),
        )
    }

    val todoItemViewModelFactory by lazy {
        TodoItemViewModelFactory(
            getTodoItemsFlowUseCase = createGetTodoItemsFlowUseCase(),
            addTodoItemUseCase = createAddTodoItemUseCase(),
            editTodoItemUseCase = createEditTodoItemUseCase(),
            deleteTodoItemUseCase = createDeleteTodoItemUseCase(),
            getTodoItemUseCase = createGetTodoItemFlowUseCase()
        )
    }
}
