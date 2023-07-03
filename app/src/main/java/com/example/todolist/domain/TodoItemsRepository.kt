package com.example.todolist.domain

import kotlinx.coroutines.flow.StateFlow

interface TodoItemsRepository {

    val todoItemsStateFlow: StateFlow<List<TodoItem>>

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(id: String)

    suspend fun editTodoItem(todoItem: TodoItem)
}
