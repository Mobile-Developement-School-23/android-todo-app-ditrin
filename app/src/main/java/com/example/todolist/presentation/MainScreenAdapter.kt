package com.example.todolist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.domain.TodoItem
import java.lang.RuntimeException

class MainScreenAdapter : RecyclerView.Adapter<MainScreenAdapter.MainScreenViewHolder>() {

    var todoList : List<TodoItem>
        set(value) = differ.submitList(value)
        get() = differ.currentList

    private val differ = AsyncListDiffer(this, MainScreenDiffUtil())

    var onTodoLongClickListener: ((TodoItem) -> Unit)? = null
    var onTodoClickListener: ((TodoItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainScreenViewHolder {
        val layout = when(viewType){
            VIEW_TYPE_COMPLETED -> R.layout.item_todo_complete
            VIEW_TYPE_NOT_COMPLETED -> R.layout.item_todo_not_complete
            else -> throw RuntimeException("Invalid type: $viewType")
        }
        val view =
            LayoutInflater.from(parent.context).inflate(
                layout,
                parent,
                false)
        return MainScreenViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = todoList[position]
        return if (item.isCompleted) {
            VIEW_TYPE_COMPLETED
        } else {
            VIEW_TYPE_NOT_COMPLETED
        }
    }

    override fun onBindViewHolder(holder: MainScreenViewHolder, position: Int) {
      //  holder.bind(todoList[position])
        val todoItem = todoList[position]
        holder.view.setOnLongClickListener {
            onTodoLongClickListener?.invoke(todoItem)
            true
        }
        holder.view.setOnClickListener {
            onTodoClickListener?.invoke(todoItem)

        }
        holder.text.text = todoItem.text
    }

    class MainScreenViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val text = view.findViewById<TextView>(R.id.text)
        val importance = view.findViewById<ImageView>(R.id.importance)

        fun bind(todoItem: TodoItem){
            text.text = todoItem.text
            if(todoItem.importance == TodoItem.Importance.LOW){
                importance.setImageResource(R.drawable.importance_low)
            }
            else if (todoItem.importance == TodoItem.Importance.HIGH){
                    importance.setImageResource(R.drawable.importance_high)
                }
            else importance.setImageResource(R.drawable.importance_common)
        }
    }

    companion object{
        const val VIEW_TYPE_COMPLETED = 0
        const val VIEW_TYPE_NOT_COMPLETED = 1
    }
}