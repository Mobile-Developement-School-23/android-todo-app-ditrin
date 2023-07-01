package com.example.todolist.domain

class GetTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {

    fun getTodoItem(id: String): TodoItem{
        return  todoItemsRepository.getTodoItem(id)
    }
}