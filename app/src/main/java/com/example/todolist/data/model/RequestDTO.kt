package com.example.todolist.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class RequestDTO(
    @SerializedName("element") val element: TodoItemDto
)