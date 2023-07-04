package com.example.todolist.data.mappers

import com.example.todolist.data.model.RequestDTO
import com.example.todolist.data.model.TodoItemDto
import com.example.todolist.domain.Importance
import com.example.todolist.domain.TodoItem


fun String.importanceCurrent(): Importance {
    return when (this) {
        "low" -> Importance.LOW
        "basic" -> Importance.COMMON
        "important" -> Importance.HIGH

        else -> throw RuntimeException("")
    }
}

fun TodoItemDto.toEntity() = TodoItem(
    id,
    text,
    importance.importanceCurrent(),
    deadline,
    done,
    created_at.toLong(),
    changed_at?.toLong()
)

fun List<TodoItemDto>.toEntity() = this.map { it.toEntity() }


fun Importance.toBody(): String = when (this) {
    Importance.LOW -> "low"
    Importance.COMMON -> "basic"
    Importance.HIGH -> "important"
}


fun TodoItem.toBody(): RequestDTO {
    val todoItemApi = TodoItemDto(
        id = id,
        text = text,
        importance = importance.toBody(),
        deadline = deadline,
        done = isCompleted,
        changed_at = (modifiedAt ?: createdAt).toInt(),
        created_at = createdAt.toInt(),
        last_updated_by = "cf1"
    )

    return RequestDTO(todoItemApi)
}