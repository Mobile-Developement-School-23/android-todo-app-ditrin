package com.example.todolist.data.datasource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.domain.Importance

@Entity(tableName = "todo_item")
data class TodoItemDbModel(
    @PrimaryKey//(autoGenerate = true)
    @ColumnInfo("id") val id: String,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("importance") val importance: Importance,
    @ColumnInfo("deadline") val deadline: Long?,
    @ColumnInfo("flag") val isCompleted: Boolean,
    @ColumnInfo("dateOfCreating") val createdAt: Long,
    @ColumnInfo("dateOfEditing") val modifiedAt: Long?,
)