package com.example.todolist.domain

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Long?,
    val isCompleted: Boolean,
    val createdAt: Long,
    val modifiedAt: Long?,
)

enum class Importance {
    LOW,
    COMMON,
    HIGH,
}
