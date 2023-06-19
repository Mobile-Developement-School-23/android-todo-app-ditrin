package com.example.todolist.domain

import androidx.lifecycle.LiveData

interface TodoItemsRepository {

    fun addTodoItem(todoItem: TodoItem)

    fun deleteTodoItem(todoItem: TodoItem)

    fun editTodoItem(todoItem: TodoItem)

    fun getTodoItem(id: String): TodoItem

    fun getTodoList(): LiveData<List<TodoItem>>
}