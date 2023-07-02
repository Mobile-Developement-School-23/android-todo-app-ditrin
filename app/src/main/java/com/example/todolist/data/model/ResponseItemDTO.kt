package com.example.todolist.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseItemDTO(
    @SerializedName("element") val todoDTO: TodoItemDto,
    @SerializedName("status") val status: String = "OK",
)