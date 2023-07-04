package com.example.todolist.domain

import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {

    fun getTodoListItem(): Flow<List<TodoItem>>

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(id: String)

    suspend fun editTodoItem(todoItem: TodoItem)

    suspend fun getTodoItemById(todoId: String): TodoItem?
}
