package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todolist.domain.TodoItem
import com.example.todolist.domain.TodoItemsRepository
import java.lang.RuntimeException
import kotlin.random.Random

object TodoItemsRepositoryImpl : TodoItemsRepository {

    private val todoListLiveData = MutableLiveData<List<TodoItem>>()

    private val todoItemsList = sortedSetOf<TodoItem>({o1, o2 -> o1.id.compareTo(o2.id)})

    init {
        for (i in 0 until todoListTemp.size) {
            todoItemsList.add(todoListTemp[i])
        }
        updateList()
    }

    override fun addTodoItem(todoItem: TodoItem) {
        if (todoItem.id == null) {
            todoItem.id = Random.nextInt().toString()
        }
        todoItem.importance = TodoItem.Importance.COMMON
        todoItem.deadline = 0
        todoItem.createdAt = 0
        todoItem.modifiedAt = null
        todoItemsList.add(todoItem)
        updateList()
    }

    override fun deleteTodoItem(todoItem: TodoItem) {
        todoItemsList.remove(todoItem)
        updateList()
    }

    override fun editTodoItem(todoItem: TodoItem) {
        val oldElement = getTodoItem(todoItem.id)
        todoItemsList.remove(oldElement)
        addTodoItem(todoItem)
    }

    override fun getTodoItem(id: String): TodoItem {
        return todoItemsList.find {
            it.id == id
        } ?: throw RuntimeException("Element with id $id not found")
    }

    override fun getTodoList(): LiveData<List<TodoItem>> {
        return todoListLiveData
    }

    private fun updateList() {
        todoListLiveData.value = todoItemsList.toList()
    }

}