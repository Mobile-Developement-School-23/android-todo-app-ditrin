package com.example.todolist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_item")
    fun getTodoList(): Flow<List<TodoItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTodoItem(todoItemDBmodel: TodoItemDbModel)

    @Query("DELETE FROM todo_item WHERE id=:id")
    fun deleteTodoItem(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun editTodoItem(todoItemDBmodel: TodoItemDbModel)

    @Query("SELECT * FROM todo_item WHERE id=:id LIMIT 1")
    fun getTodoItemById(id: String): TodoItemDbModel
}