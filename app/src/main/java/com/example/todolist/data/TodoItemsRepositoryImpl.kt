package com.example.todolist.data

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.todolist.TodoListApplication
import com.example.todolist.data.mappers.TodoListMapper
import com.example.todolist.data.mappers.toBody
import com.example.todolist.data.mappers.toEntity
import com.example.todolist.data.model.NetworkError
import com.example.todolist.data.model.NetworkException
import com.example.todolist.data.model.NetworkSuccess
import com.example.todolist.data.model.handleApi
import com.example.todolist.data.remote.Networking
import com.example.todolist.data.remote.TodoListAPI
import com.example.todolist.domain.TodoItem
import com.example.todolist.domain.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class TodoItemsRepositoryImpl(
    private val todoListApi: TodoListAPI = Networking.todoListAPI,
    private val context: Context
) : TodoItemsRepository {

   //    private val application = TodoListApplication()

  //  private val todoListDao = AppDataBase.getInstance(application = Application()).todoListDao()

    companion object {
        const val KEY_REVISION = "key_revision"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            "shared_preference", Context.MODE_PRIVATE
        )
    }

    private val editRevision by lazy {
        sharedPreferences.edit()
    }

    private val mapper = TodoListMapper()

    override fun getTodoListItem(): Flow<List<TodoItem>> {

        return flow {

            when (val response = handleApi { todoListApi.getTodoList() }) {
                is NetworkError -> {

                }

                is NetworkException -> {
                    //            todoListDao.getTodoList()
                }

                is NetworkSuccess -> {
                    editRevision.putInt(KEY_REVISION, response.data.revision).apply()
                    emit(response.data.list.toEntity())
                }
            }

        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addTodoItem(todoItem: TodoItem) {

        val revision = sharedPreferences.getInt(KEY_REVISION, -1)

        when (val response = handleApi { todoListApi.addTodoItem(todoItem.toBody(), revision) }) {
            is NetworkError -> {
                Log.d("testConnectionNetwork", "NetworkError add item todo")
            }

            is NetworkException -> {
                //        todoListDao.addTodoItem(mapper.mapEntityToDbModel(todoItem))
            }

            is NetworkSuccess -> {
                editRevision.putInt(KEY_REVISION, response.data.revision).apply()
            }
        }
    }

    override suspend fun deleteTodoItem(id: String) {

        val revision = sharedPreferences.getInt(KEY_REVISION, -1)
        when (val response = handleApi { todoListApi.deleteTodoItem(id, revision) }) {
            is NetworkError -> {
                Log.d("testConnectionNetwork", "NetworkError delete item todo")
            }

            is NetworkException -> {
                //          todoListDao.deleteTodoItem(id)
            }

            is NetworkSuccess -> {
                editRevision.putInt(KEY_REVISION, response.data.revision).apply()
            }
        }
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {

        val revision = sharedPreferences.getInt(KEY_REVISION, -1)
        when (val response = handleApi {
            todoListApi.updateTodoItem(todoItem.id, todoItem.toBody(), revision)
        }) {
            is NetworkError -> {
            }

            is NetworkException -> {
                //        todoListDao.addTodoItem(mapper.mapEntityToDbModel(todoItem))
            }

            is NetworkSuccess -> {
                editRevision.putInt(KEY_REVISION, response.data.revision).apply()
            }
        }
    }

    override suspend fun getTodoItemById(id: String): TodoItem? {

        return when (val response = handleApi { todoListApi.getTodoItem(id) }) {
            is NetworkError -> {
                Log.d("testConnectionNetwork", "NetworkError getTodoItemById item todo")
                null
            }
            is NetworkException -> {
                Log.d("testConnectionNetwork", " NetworkException getTodoItemById item todo")
                null
            }
            is NetworkSuccess -> {
                response.data.element.toEntity()
            }
        }

    }
}
