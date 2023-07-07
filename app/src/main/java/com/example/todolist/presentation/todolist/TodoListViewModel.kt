package com.example.todolist.presentation.todolist

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.TodoListApplication
import com.example.todolist.domain.DeleteTodoItemUseCase
import com.example.todolist.domain.EditTodoItemUseCase
import com.example.todolist.domain.GetTodoItemsFlowUseCase
import com.example.todolist.domain.TodoItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoListViewModel(
    application: TodoListApplication,
    private val getTodoItemsFlowUseCase: GetTodoItemsFlowUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
) : AndroidViewModel(application) {

    private val navigationActionMutableFlow = MutableSharedFlow<TodoItemsNavigationAction>()
    val navigationActionFlow = navigationActionMutableFlow.asSharedFlow()

    private val isEyeVisibleMutableFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isEyeVisibleFlow = isEyeVisibleMutableFlow.asStateFlow()

    val allTodoItemsFlow = getTodoItemsFlowUseCase.getTodoItemsFlow()

    val todoItemsFlow = combine(
        allTodoItemsFlow,
        isEyeVisibleFlow,
        transform = { todoItems, isEyeVisible ->
            if (isEyeVisible) {
                todoItems
            } else {
                todoItems.filter { todoItem -> !todoItem.isCompleted }
            }
        }
    )

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        Log.e("ERROR", "$error")
    }

    fun onEyeClicked() {
        isEyeVisibleMutableFlow.update { !it }

    }

    fun onTodoItemClicked(todoItem: TodoItem) {
        viewModelScope.launch {
            val action = TodoItemsNavigationAction.OpenEditTodoItemScreen(todoItem)
            navigationActionMutableFlow.emit(action)
        }
    }

    fun onTodoItemLongClicked(todoItem: TodoItem) {
        changeCompletedState(todoItem)
    }

    fun onLeftToRightSwiped(todoItem: TodoItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            deleteTodoItemUseCase.deleteTodoItem(todoItem.id)
        }
    }

    fun onRightToLeftSwiped(todoItem: TodoItem) {
        changeCompletedState(todoItem)
    }

    fun onAddItemClicked() {
        viewModelScope.launch {
            val action = TodoItemsNavigationAction.OpenAddTodoItemScreen
            navigationActionMutableFlow.emit(action)
        }
    }

    private fun changeCompletedState(todoItem: TodoItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val newItem = todoItem.copy(isCompleted = !todoItem.isCompleted)
            editTodoItemUseCase.editTodoItem(newItem)
        }
    }
}
