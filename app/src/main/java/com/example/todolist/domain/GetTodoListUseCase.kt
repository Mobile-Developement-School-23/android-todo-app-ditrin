package com.example.todolist.domain

import androidx.lifecycle.LiveData

class GetTodoListUseCase(private val todoItemsRepository: TodoItemsRepository) {

    fun getTodoList(): LiveData<List<TodoItem>>{
        return todoItemsRepository.getTodoList()
    }
}