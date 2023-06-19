package com.example.todolist.domain

class DeleteTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {

    fun deleteTodoItem(todoItem: TodoItem){
        todoItemsRepository.deleteTodoItem(todoItem)
    }
}