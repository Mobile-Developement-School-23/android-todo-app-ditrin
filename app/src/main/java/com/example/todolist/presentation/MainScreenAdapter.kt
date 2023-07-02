package com.example.todolist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todolist.R
import com.example.todolist.domain.TodoItem

class MainScreenAdapter :
    ListAdapter<TodoItem, MainScreenViewHolder>(MainScreenDiffItemCallback()) {

    var onTodoLongClickListener: ((TodoItem) -> Unit)? = null
    var onTodoClickListener: ((TodoItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainScreenViewHolder {
        return when (viewType) {
            VIEW_TYPE_COMPLETED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_todo_complete, parent, false)
                MainScreenViewHolder(view)
            }

            VIEW_TYPE_NOT_COMPLETED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_todo_not_complete, parent, false)
                MainScreenViewHolder(view)
            }

            else -> throw RuntimeException("Invalid type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isCompleted) {
            VIEW_TYPE_COMPLETED
        } else {
            VIEW_TYPE_NOT_COMPLETED
        }
    }

    override fun onBindViewHolder(holder: MainScreenViewHolder, position: Int) {
        holder.bind(getItem(position))
        val todoItem = getItem(position)

        holder.view.setOnLongClickListener {
            onTodoLongClickListener?.invoke(todoItem)
            true
        }
        holder.view.setOnClickListener {
            onTodoClickListener?.invoke(todoItem)
        }
        holder.text.text = todoItem.text
    }

    companion object {
        const val VIEW_TYPE_COMPLETED = 0
        const val VIEW_TYPE_NOT_COMPLETED = 1
    }

    fun setOnClickListener(listener: (TodoItem) -> Unit) {
        onTodoClickListener = listener
    }
}