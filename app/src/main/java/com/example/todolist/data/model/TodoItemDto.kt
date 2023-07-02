package com.example.todolist.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemDto(
    @SerialName("changed_at") val changed_at: Int?,
    @SerialName("created_at") val created_at: Int,
    @SerialName("deadline") val deadline: Long?,
    @SerialName("done") val done: Boolean,
    @SerialName("id") val id: String,
    @SerialName("importance") val importance: String,
    @SerialName("last_updated_by") val last_updated_by: String,
    @SerialName("text") val text: String
)