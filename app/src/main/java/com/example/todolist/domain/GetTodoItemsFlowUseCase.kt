package com.example.todolist.domain

import kotlinx.coroutines.flow.StateFlow

class GetTodoItemsFlowUseCase(private val todoItemsRepository: TodoItemsRepository) {

     fun getTodoItemsFlow(): StateFlow<List<TodoItem>> {
        return todoItemsRepository.todoItemsStateFlow
    }
}