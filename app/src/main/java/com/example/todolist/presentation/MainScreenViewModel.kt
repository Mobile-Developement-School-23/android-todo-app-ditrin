package com.example.todolist.presentation

import androidx.lifecycle.ViewModel
import com.example.todolist.data.TodoItemsRepositoryImpl
import com.example.todolist.domain.DeleteTodoItemUseCase
import com.example.todolist.domain.EditTodoItemUseCase
import com.example.todolist.domain.GetTodoListUseCase
import com.example.todolist.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainScreenViewModel: ViewModel() {

    private val repository = TodoItemsRepositoryImpl

    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)

    val todoList = getTodoListUseCase.getTodoList()

    private val _changeVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val changeVisible get() = _changeVisible.asStateFlow()

    var visible: Boolean = false
        set(value) {
            field = value
            _changeVisible.tryEmit(value)
        }

    fun deleteTodoItem(todoItem: TodoItem){
        deleteTodoItemUseCase.deleteTodoItem(todoItem)
    }

    fun changeCompletedState(todoItem: TodoItem){
        val newItem = todoItem.copy(isCompleted = !todoItem.isCompleted)
        editTodoItemUseCase.editTodoItem(newItem)
    }
}