package com.example.todolist.presentation.todoitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class TodoItemViewModel(
    private val getTodoItemsFlowUseCase: GetTodoItemsFlowUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val getTodoItemUseCase: GetTodoItemUseCase
) : ViewModel() {

    private val itemIdMutableFlow = MutableStateFlow<String?>(null)

    val todoItemFlowCombine = combine(
        getTodoItemsFlowUseCase.getTodoItemsFlow(),
        itemIdMutableFlow,
        transform = { todoItems, itemId ->
            todoItems.firstOrNull { it.id == itemId }
        }
    )

    val todoItemFlow = getTodoItemsFlowUseCase.getTodoItemsFlow()
        .map { it.findLast { id -> id.id == itemIdMutableFlow.value } }

    private val _errorInputText = MutableStateFlow(false)
    val errorInputText = _errorInputText.asStateFlow()

    private val _todoItem = MutableStateFlow<TodoItem?>(null)
    val todoItem: StateFlow<TodoItem?>
        get() = _todoItem.asStateFlow()

    private val _todoId = MutableStateFlow<String?>(null)

    private val _closeScreen = MutableLiveData<Unit>()
    val closeScreen: LiveData<Unit>
        get() = _closeScreen

    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline get() = _deadline.asStateFlow()

    private val todoText = MutableStateFlow("")

    fun onItemIdGot(id: String) {
        itemIdMutableFlow.value = id
    }

    fun setTodoItemId(id: String) {
        _todoId.tryEmit(id)
        viewModelScope.launch {
            _todoItem.emit(getTodoItemUseCase(id))
        }
    }

    fun setTodoItem(todoItem: TodoItem) {
        _todoItem.tryEmit(todoItem)
    }

    fun setText(text: String) {
        todoText.tryEmit(text)
        _errorInputText.tryEmit(false)
    }

    fun setDeadline(deadline: Long?) {
        _deadline.tryEmit(deadline)
    }

    fun addTodoItem(inputText: String?, importance: Importance, createdAt: Long) {
        val id = Random.nextInt().toString()
        val text = parseName(inputText)
        val deadline = _deadline
        val importance = importance
        val isComplete = false
        val createdAt = createdAt
        val modifiedAt = null
        val fieldValid = validateInput(text)
        if (fieldValid) {
            val todoItem =
                TodoItem(id, text, importance, deadline.value, isComplete, createdAt, modifiedAt)
            viewModelScope.launch {
                addTodoItemUseCase.addTodoItem(todoItem)
            }
            closeScreen()
        }
    }

    fun editTodoItem(inputText: String?, deadline: Long, importance: Importance) {
        val text = parseName(inputText)
        val deadline = _deadline
        val importance = importance

        _todoItem.value?.let {
            val todoItem = it.copy(
                text = text,
                deadline = deadline.value,
                importance = importance
            )

            viewModelScope.launch {
                editTodoItemUseCase.editTodoItem(todoItem)
            }
            closeScreen()
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

    private fun validateInput(text: String): Boolean {
        var result = true
        if (text.isEmpty()) {
            _errorInputText.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputText() {
        _errorInputText.value = false
    }

    private fun closeScreen() {
        _closeScreen.value = Unit
    }
}