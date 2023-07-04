package com.example.todolist.presentation.mainscreen

import com.example.todolist.domain.TodoItem

sealed interface TodoItemsNavigationAction {

    object OpenAddTodoItemScreen : TodoItemsNavigationAction

    data class OpenEditTodoItemScreen(
        val todoItem: TodoItem,
    ) : TodoItemsNavigationAction
}
