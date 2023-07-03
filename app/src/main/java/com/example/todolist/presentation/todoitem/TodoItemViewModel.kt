package com.example.todolist.presentation.todoitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.AddTodoItemUseCase
import com.example.todolist.domain.DeleteTodoItemUseCase
import com.example.todolist.domain.EditTodoItemUseCase
import com.example.todolist.domain.GetTodoItemsFlowUseCase
import com.example.todolist.domain.Importance
import com.example.todolist.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Locale
import kotlin.random.Random

class TodoItemViewModel(
    private val getTodoItemsFlowUseCase: GetTodoItemsFlowUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
) : ViewModel() {

    private val itemIdMutableFlow = MutableStateFlow<String?>(null)

    val todoItemFlow = combine(
        getTodoItemsFlowUseCase.getTodoItemsFlow(),
        itemIdMutableFlow,
        transform = { todoItems, itemId ->
            todoItems.firstOrNull { it.id == itemId }
        }
    )

    private val _errorInputText = MutableStateFlow(false)
    val errorInputText = _errorInputText.asStateFlow()

    private val _todoItem = MutableLiveData<TodoItem>()
    val todoItem: LiveData<TodoItem>
        get() = _todoItem

    private val _closeScreen = MutableLiveData<Unit>()
    val closeScreen: LiveData<Unit>
        get() = _closeScreen

    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline get() = _deadline.asStateFlow()

    private val todoText = MutableStateFlow("")

    fun onItemIdGot(id: String) {
        itemIdMutableFlow.value = id
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
            viewModelScope.launch {
                addTodoItemUseCase.addTodoItem(todoItem)
            }
            closeScreen()
        }
    }

    fun setTaskText(text: String) {
        todoText.tryEmit(text)
        _errorInputText.tryEmit(false)
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
                viewModelScope.launch {
                    editTodoItemUseCase.editTodoItem(item)
                }
                closeScreen()
            }
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            deleteTodoItemUseCase.deleteTodoItem(todoItem.id)
        }
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