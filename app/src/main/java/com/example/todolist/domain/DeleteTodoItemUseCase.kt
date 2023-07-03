package com.example.todolist.domain

class DeleteTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {

    suspend fun deleteTodoItem(id: String) {
        todoItemsRepository.deleteTodoItem(id)
    }
}