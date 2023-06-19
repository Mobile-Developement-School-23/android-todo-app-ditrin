package com.example.todolist.domain

data class TodoItem (
    var id: String,
    val text: String,
    var importance:Importance,
    var deadline: Long?,
    var isCompleted: Boolean,
    var createdAt: Long,
    var modifiedAt: Long?,
    ){
    enum class Importance{
        LOW,
        COMMON,
        HIGH,
    }
}