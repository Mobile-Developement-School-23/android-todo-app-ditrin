package com.example.todolist.presentation

import androidx.lifecycle.ViewModel
import com.example.todolist.data.TodoItemsRepositoryImpl
import com.example.todolist.domain.DeleteTodoItemUseCase
import com.example.todolist.domain.EditTodoItemUseCase
import com.example.todolist.domain.GetTodoListUseCase
import com.example.todolist.domain.TodoItem

class MainScreenViewModel: ViewModel() {

    private val repository = TodoItemsRepositoryImpl

    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)

    val todoList = getTodoListUseCase.getTodoList()

    fun deleteTodoItem(todoItem: TodoItem){
        deleteTodoItemUseCase.deleteTodoItem(todoItem)
    }

    fun changeCompletedState(todoItem: TodoItem){
        val newItem = todoItem.copy(isCompleted = !todoItem.isCompleted)
        editTodoItemUseCase.editTodoItem(newItem)
    }
}