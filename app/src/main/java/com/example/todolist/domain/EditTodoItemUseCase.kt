package com.example.todolist.domain

class EditTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository){

    fun editTodoItem(todoItem: TodoItem){
        todoItemsRepository.editTodoItem(todoItem)
    }
}