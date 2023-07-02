package com.example.todolist.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseItemDTO(
    @SerializedName("element") val todoItemDto: TodoItemDto,
    @SerializedName("status") val status: String = "OK",
)