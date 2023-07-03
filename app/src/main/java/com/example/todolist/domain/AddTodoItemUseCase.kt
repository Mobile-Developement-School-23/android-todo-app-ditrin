package com.example.todolist.domain

class AddTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {

    suspend fun addTodoItem(todoItem: TodoItem){
        todoItemsRepository.addTodoItem(todoItem)
    }
}