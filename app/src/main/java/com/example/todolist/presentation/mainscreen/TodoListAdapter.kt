package com.example.todolist.presentation.mainscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.ItemTodoCompleteBinding
import com.example.todolist.databinding.ItemTodoNotCompleteBinding
import com.example.todolist.domain.Importance
import com.example.todolist.domain.TodoItem

class TodoListAdapter(
    private val onItemClick: (TodoItem) -> Unit,
    private val onItemLongClick: (TodoItem) -> Unit,
) : ListAdapter<TodoItem, RecyclerView.ViewHolder>(TodoListDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_COMPLETED -> {
                val binding = ItemTodoCompleteBinding.inflate(layoutInflater, parent, false)
                CompletedViewHolder(binding)
            }
            VIEW_TYPE_NOT_COMPLETED -> {
                val binding = ItemTodoNotCompleteBinding.inflate(layoutInflater, parent, false)
                NotCompletedViewHolder(binding)
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CompletedViewHolder -> holder.bind(getItem(position))
            is NotCompletedViewHolder -> holder.bind(getItem(position))
        }
    }

    inner class CompletedViewHolder(
        private val binding: ItemTodoCompleteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var todoItem: TodoItem? = null

        init {
            binding.root.setOnLongClickListener {
                todoItem
                    ?.let(onItemLongClick)
                    ?.let { true }
                    ?: false
            }
            binding.root.setOnClickListener {
                todoItem?.let(onItemClick)
            }
        }

        fun bind(todoItem: TodoItem) {
            this.todoItem = todoItem

            binding.text.text = todoItem.text

            when (todoItem.importance) {
                Importance.LOW -> {
                    binding.importance.setImageResource(R.drawable.importance_low)
                }
                Importance.HIGH -> {
                    binding.importance.setImageResource(R.drawable.importance_high)
                }
                else -> binding.importance.setImageDrawable(null)
            }
        }
    }

    inner class NotCompletedViewHolder(
        private val binding: ItemTodoNotCompleteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var todoItem: TodoItem? = null

        init {
            binding.root.setOnLongClickListener {
                todoItem
                    ?.let(onItemLongClick)
                    ?.let { true }
                    ?: false
            }
            binding.root.setOnClickListener {
                todoItem?.let(onItemClick)
            }
        }

        fun bind(todoItem: TodoItem) {
            this.todoItem = todoItem

            binding.text.text = todoItem.text

            when (todoItem.importance) {
                Importance.LOW -> {
                    binding.importance.setImageResource(R.drawable.importance_low)
                }
                Importance.HIGH -> {
                    binding.importance.setImageResource(R.drawable.importance_high)
                }
                else -> binding.importance.setImageDrawable(null)
            }
        }
    }

    companion object {

        private const val VIEW_TYPE_COMPLETED = 0
        private const val VIEW_TYPE_NOT_COMPLETED = 1
    }
}