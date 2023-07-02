package com.example.todolist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoDTO(
    @SerialName("element") val element: TodoItemDto
)