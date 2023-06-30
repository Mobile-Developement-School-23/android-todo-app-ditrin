package com.example.todolist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TodoItemsRepositoryImpl
import com.example.todolist.domain.AddTodoItemUseCase
import com.example.todolist.domain.DeleteTodoItemUseCase
import com.example.todolist.domain.EditTodoItemUseCase
import com.example.todolist.domain.GetTodoItemUseCase
import com.example.todolist.domain.Importance
import com.example.todolist.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Locale
import kotlin.random.Random

class TodoItemViewModel : ViewModel() {

    private val repository = TodoItemsRepositoryImpl

    private val _errorInputText = MutableLiveData<Boolean>()
    val errorInputText: LiveData<Boolean> = _errorInputText

    private val _todoItem = MutableLiveData<TodoItem>()
    val todoItem: LiveData<TodoItem>
        get() = _todoItem

    private val getTodoItemUseCase = GetTodoItemUseCase(repository)
    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(repository)

    private val _closeScreen = MutableLiveData<Unit>()
    val closeScreen: LiveData<Unit>
        get() = _closeScreen

    private val _todoItemState = MutableStateFlow<TodoItem?>(null)
    val todoItemState = _todoItemState.asStateFlow()

    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline get() = _deadline.asStateFlow()

    fun getTodoItemId(id: String) {
        val item = getTodoItemUseCase.getTodoItem(id)
        _todoItem.value = item
    }

    fun addTodoItem(inputText: String?, inputDate: Long, importance: Importance, createdAt: Long) {
        val id = Random.nextInt().toString()
        val text = parseName(inputText)
        val deadline = inputDate
        val importance = importance
        val isComplete = false
        val createdAt = createdAt
        val modifiedAt = null
        val fieldValid = validateInput(text)
        if (fieldValid) {
            val todoItem =
                TodoItem(id, text, importance, deadline, isComplete, createdAt, modifiedAt)
            addTodoItemUseCase.addTodoItem(todoItem)
            closeScreen()
        }
    }

    fun editTodoItem(inputText: String?, deadline: Long, importance: Importance, createdAt: Long) {
        val text = parseName(inputText)
        val deadline = deadline
        val importance = importance
        val createdAt = createdAt
        val fieldValid = validateInput(text)
        if (fieldValid) {
            _todoItem.value?.let {
                val item = it.copy(
                    text = text,
                    deadline = deadline,
                    importance = importance)
                editTodoItemUseCase.editTodoItem(item)
                closeScreen()
            }
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        deleteTodoItemUseCase.deleteTodoItem(todoItem)
    }

    private fun parseName(inputText: String?): String {
        return inputText?.trim() ?: ""
    }

    fun saveTask(inputText: String?, deadline: Long, importance: Importance, dateOfCreating: Long) {
        viewModelScope.launch {
            editTodoItem(inputText, deadline, importance, dateOfCreating)
        }
    }

    private fun validateInput(text: String): Boolean {
        var result = true
        if (text.isBlank()) {
            _errorInputText.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputText() {
        _errorInputText.value = false
    }

    fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
        val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
        return df.format(this)
    }

    private fun closeScreen() {
        _closeScreen.value = Unit
    }


    fun setDeadline(deadline: Long?) {
        _deadline.tryEmit(deadline)
    }
}