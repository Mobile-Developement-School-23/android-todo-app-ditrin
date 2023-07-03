package com.example.todolist

import android.app.Application
import com.example.todolist.data.TodoItemsRepositoryImpl
import com.example.todolist.domain.AddTodoItemUseCase
import com.example.todolist.domain.DeleteTodoItemUseCase
import com.example.todolist.domain.EditTodoItemUseCase
import com.example.todolist.domain.GetTodoItemsFlowUseCase
import com.example.todolist.domain.TodoItemsRepository
import com.example.todolist.presentation.mainscreen.TodoListViewModelFactory

class TodoListApplication : Application() {

    private val todoItemsRepository: TodoItemsRepository by lazy {
        TodoItemsRepositoryImpl()
    }

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

    val todoItemsViewModelFactory by lazy {
        TodoListViewModelFactory(
            getTodoItemsFlowUseCase = createGetTodoItemsFlowUseCase(),
            deleteTodoItemUseCase = createDeleteTodoItemUseCase(),
            editTodoItemUseCase = createEditTodoItemUseCase(),
        )
    }
}
