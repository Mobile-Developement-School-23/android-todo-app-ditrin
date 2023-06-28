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
        val layout = when (viewType) {
            VIEW_TYPE_COMPLETED -> R.layout.item_todo_complete
            VIEW_TYPE_NOT_COMPLETED -> R.layout.item_todo_not_complete
            else -> throw RuntimeException("Invalid type: $viewType")
        }
        val view =
            LayoutInflater.from(parent.context).inflate(
                layout,
                parent,
                false
            )
        return MainScreenViewHolder(view)
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
        //  holder.bind(todoList[position])
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
}