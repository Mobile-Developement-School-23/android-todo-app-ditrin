package com.example.todolist.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.domain.Importance
import com.example.todolist.domain.TodoItem

class MainScreenViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val text = view.findViewById<TextView>(R.id.text)
    val importance = view.findViewById<ImageView>(R.id.importance)

    fun bind(todoItem: TodoItem){
        text.text = todoItem.text
        if(todoItem.importance == Importance.LOW){
            importance.setImageResource(R.drawable.importance_low)
        }
        else if (todoItem.importance == Importance.HIGH){
            importance.setImageResource(R.drawable.importance_high)
        }
        else importance.setImageDrawable(null)
    }
}