package com.example.todolist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RequestDTO (
    @SerialName("status") val status: String,
    @SerialName("element") val element: TodoItemDto
)