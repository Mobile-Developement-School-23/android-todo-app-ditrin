package com.example.todolist.presentation.compose

import com.example.todolist.domain.Importance

sealed interface Action

sealed interface Screen : Action

object CloseScreen : Screen
object SaveScreen : Screen
class TextChange(val text: String) : Screen
class DeadlineChange(val deadline: Long?) : Screen
class ImportanceChange(val importance: Importance) : Screen
object ImportanceClick: Screen
object CloseImportanceDio: Screen

object Delete : Screen

object ConsentToOffline : Screen

data class TodoScreenState(
    var id: String = "",
    var text: String = "",
    var deadline: Long? = null,
    var importance: Importance = Importance.COMMON,
    var nullTextChange: Boolean = false,
    var error: Boolean = false,
    var isLoading: Boolean = false,
    var isAddScreen: Boolean = true,
    var isCompleted: Boolean = false,
    var dateOfCreating: Long = 0L,
    var dateOnChanged: Long? = null,
    var isNotInternet: Boolean = false,
    var isImportanceDioOpen: Boolean = false
)