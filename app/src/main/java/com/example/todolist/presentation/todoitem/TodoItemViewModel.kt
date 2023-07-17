package com.example.todolist.presentation.todoitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.*
import com.example.todolist.presentation.compose.Action
import com.example.todolist.presentation.compose.CloseImportanceDio
import com.example.todolist.presentation.compose.CloseScreen
import com.example.todolist.presentation.compose.ConsentToOffline
import com.example.todolist.presentation.compose.DeadlineChange
import com.example.todolist.presentation.compose.Delete
import com.example.todolist.presentation.compose.ImportanceChange
import com.example.todolist.presentation.compose.ImportanceClick
import com.example.todolist.presentation.compose.SaveScreen
import com.example.todolist.presentation.compose.TodoScreenState
import com.example.todolist.presentation.compose.TextChange
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class TodoItemViewModel(
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val getTodoItemUseCase: GetTodoItemUseCase
) : ViewModel() {

    private val _errorInputText = MutableStateFlow(false)
    val errorInputText = _errorInputText.asStateFlow()

    private val _todoItem = MutableStateFlow<TodoItem?>(null)
    val todoItem: StateFlow<TodoItem?>
        get() = _todoItem.asStateFlow()

    private val _todoId = MutableStateFlow<String?>(null)

    private val _closeScreen = MutableStateFlow(false)
    val closeScreen get() = _closeScreen.asStateFlow()

    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline get() = _deadline.asStateFlow()

    val state: MutableStateFlow<TodoScreenState> = MutableStateFlow(TodoScreenState())

    fun onAction(action: Action) {

        when (action) {

            is SaveScreen -> {
                state.update {
                    it.copy(isLoading = true)
                }
                if (state.value.id == "") {
                    addTodoItem(state.value)
                } else
                    editTodoItem(state.value)
            }

            is TextChange -> {
                state.update {
                    it.copy(text = action.text, nullTextChange = false)
                }
            }

            is DeadlineChange -> {
                state.update {
                    it.copy(deadline = action.deadline)
                }
            }

            is ImportanceChange -> {
                state.update {
                    it.copy(importance = action.importance, isImportanceDioOpen = false)
                }
            }

            is ConsentToOffline -> {
                onAction(CloseScreen)
            }

            Delete -> {
                deleteTodoItem(state.value)
            }

            ImportanceClick -> {
                state.update {
                    it.copy(isImportanceDioOpen = !it.isImportanceDioOpen)
                }
            }

            CloseImportanceDio -> {
                state.update {
                    it.copy(isImportanceDioOpen = false)
                }
            }

            CloseScreen ->
                _closeScreen.tryEmit(true)
        }
    }

    fun setTodoItemId(id: String) {
        _todoId.tryEmit(id)
        viewModelScope.launch {
            val item = getTodoItemUseCase(id)
            if (item != null) {
                state.update {
                    it.copy(
                        item.id,
                        item.text,
                        item.deadline,
                        item.importance,
                        false,
                        false,
                        false,
                        false,
                        false,
                        item.createdAt,
                        item.modifiedAt,
                        false,
                        false
                    )
                }
            }
        }
    }

    fun addTodoItem(state: TodoScreenState) {
        val id = Random.nextInt().toString()
        val text = state.text
        val deadline = state.deadline
        val importance = state.importance
        val isComplete = state.isCompleted
        val createdAt = state.dateOfCreating
        val modifiedAt = state.dateOnChanged
        val fieldValid = validateInput(text)
        if (fieldValid) {
            val todoItem =
                TodoItem(id, text, importance, deadline, isComplete, createdAt, modifiedAt)
            viewModelScope.launch {
                addTodoItemUseCase.addTodoItem(todoItem)
            }
        }
        onAction(CloseScreen)
    }

    fun editTodoItem(state: TodoScreenState) {
        viewModelScope.launch {
            editTodoItemUseCase.editTodoItem(
                TodoItem(
                    state.id,
                    state.text,
                    state.importance,
                    state.deadline,
                    state.isCompleted,
                    state.dateOfCreating,
                    state.dateOnChanged
                )
            )
        }
        onAction(CloseScreen)
    }

    fun deleteTodoItem(state: TodoScreenState) {
        viewModelScope.launch {
            deleteTodoItemUseCase.deleteTodoItem(state.id)
        }
    }

    private fun validateInput(text: String): Boolean {
        var result = true
        if (text.isEmpty()) {
            _errorInputText.value = true
            result = false
        }
        return result
    }
}