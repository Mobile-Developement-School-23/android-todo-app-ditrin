package com.example.todolist.presentation.todoitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.domain.*

class TodoItemViewModelFactory(
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val getTodoItemUseCase: GetTodoItemUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TodoItemViewModel(
            addTodoItemUseCase,
            editTodoItemUseCase,
            deleteTodoItemUseCase,
            getTodoItemUseCase
        ) as? T
            ?: throw ClassCastException("Factory for $modelClass does not exist!")
    }
}