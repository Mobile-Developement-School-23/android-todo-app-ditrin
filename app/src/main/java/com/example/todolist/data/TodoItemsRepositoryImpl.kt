package com.example.todolist.data

import com.example.todolist.data.remote.Networking
import com.example.todolist.data.remote.TodoListAPI
import com.example.todolist.domain.TodoItem
import com.example.todolist.domain.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class TodoItemsRepositoryImpl(
    private val todoListApi: TodoListAPI = Networking.todoListAPI,
) : TodoItemsRepository {

    private val todoItemsMutableStateFlow = MutableStateFlow(todoListMocked.sortedBy { it.id })
    override val todoItemsStateFlow = todoItemsMutableStateFlow.asStateFlow()

    override suspend fun addTodoItem(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            todoItemsMutableStateFlow.update { current ->
                val newTodoItems = current.toMutableList()
                newTodoItems.add(todoItem)

                newTodoItems.sortedBy { it.id }
            }
        }
    }

    override suspend fun deleteTodoItem(id: String) {
        withContext(Dispatchers.IO) {
            todoItemsMutableStateFlow.update { current ->
                val newTodoItems = current.toMutableList()
                newTodoItems.removeIf { it.id == id }

                newTodoItems.sortedBy { it.id }
            }
        }
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        withContext(Dispatchers.IO) {
            todoItemsMutableStateFlow.update { current ->
                val newTodoItems = current.toMutableList()

                if (newTodoItems.removeIf { it.id == todoItem.id }) {
                    newTodoItems.add(todoItem)
                }

                newTodoItems.sortedBy { it.id }
            }
        }
    }
}
