package com.example.todolist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.TodoListApplication

@Database(entities = [TodoItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun todoListDao(): TodoListDao

    companion object {
        private var INSTANCE: AppDataBase? = null
        private val LOCK = Any()
        private const val DB_NAME = "todo_item.db"

        fun getInstance(application: Application): AppDataBase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDataBase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}