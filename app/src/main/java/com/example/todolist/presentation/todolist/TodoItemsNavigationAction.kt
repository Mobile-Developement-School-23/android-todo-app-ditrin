package com.example.todolist.presentation.todolist

import com.example.todolist.domain.TodoItem

sealed interface TodoItemsNavigationAction {

    object OpenAddTodoItemScreen : TodoItemsNavigationAction

    data class OpenEditTodoItemScreen(
        val todoItem: TodoItem,
    ) : TodoItemsNavigationAction
}
