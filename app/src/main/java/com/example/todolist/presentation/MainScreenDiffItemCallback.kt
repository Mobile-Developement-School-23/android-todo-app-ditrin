package com.example.todolist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.todolist.domain.TodoItem

class MainScreenDiffItemCallback: DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}