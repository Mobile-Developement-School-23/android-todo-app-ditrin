package com.example.todolist.presentation.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.TodoListApplication
import com.example.todolist.domain.DeleteTodoItemUseCase
import com.example.todolist.domain.EditTodoItemUseCase
import com.example.todolist.domain.GetTodoItemsFlowUseCase

class TodoListViewModelFactory(
    private val getTodoItemsFlowUseCase: GetTodoItemsFlowUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TodoListViewModel(
            getTodoItemsFlowUseCase = getTodoItemsFlowUseCase,
            deleteTodoItemUseCase = deleteTodoItemUseCase,
            editTodoItemUseCase = editTodoItemUseCase,
            application = TodoListApplication()
        ) as? T
            ?: throw ClassCastException("Factory for $modelClass does not exist!")
    }
}
