package com.example.todolist.domain

class GetTodoItemUseCase(private val repository: TodoItemsRepository) {

    suspend operator fun invoke(todoId: String) = repository.getTodoItemById(todoId)

}