package com.example.todolist.data.mappers

import com.example.todolist.data.TodoItemDbModel
import com.example.todolist.domain.TodoItem


class TodoListMapper {
    fun mapEntityToDbModel(todoItem: TodoItem) = TodoItemDbModel(
        id = todoItem.id,
        text = todoItem.text,
        importance = todoItem.importance,
        deadline = todoItem.deadline,
        isCompleted = todoItem.isCompleted,
        createdAt = todoItem.createdAt,
        modifiedAt = todoItem.modifiedAt
    )

    fun mapDbModelToEntity(todoItemDbModel: TodoItemDbModel) = TodoItem(
        id = todoItemDbModel.id,
        text = todoItemDbModel.text,
        importance = todoItemDbModel.importance,
        deadline = todoItemDbModel.deadline,
        isCompleted = todoItemDbModel.isCompleted,
        createdAt = todoItemDbModel.createdAt,
        modifiedAt = todoItemDbModel.modifiedAt
    )

    fun mapListDbModelToListEntity(list: List<TodoItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}