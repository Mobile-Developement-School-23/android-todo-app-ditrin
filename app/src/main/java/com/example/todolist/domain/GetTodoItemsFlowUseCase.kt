package com.example.todolist.domain

import kotlinx.coroutines.flow.Flow

class GetTodoItemsFlowUseCase(private val todoItemsRepository: TodoItemsRepository) {

     fun getTodoItemsFlow(): Flow<List<TodoItem>> {
        return todoItemsRepository.getTodoListItem()
    }

}