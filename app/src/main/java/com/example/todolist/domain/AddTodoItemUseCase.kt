package com.example.todolist.domain

class AddTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {

    fun addTodoItem(todoItem: TodoItem){
        todoItemsRepository.addTodoItem(todoItem)
    }
}