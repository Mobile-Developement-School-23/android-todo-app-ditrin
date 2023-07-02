package com.example.todolist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseListDTO(
    @SerialName("list") val list: List<TodoItemDto>,
    @SerialName("revision") val revision: Int,
    @SerialName("status") val status: String
)