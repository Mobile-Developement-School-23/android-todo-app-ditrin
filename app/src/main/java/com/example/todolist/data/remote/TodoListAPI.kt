package com.example.todolist.data.remote

import com.example.todolist.data.model.RequestDTO
import com.example.todolist.data.model.ResponseItemDTO
import com.example.todolist.data.model.ResponseListDTO
import retrofit2.Response
import retrofit2.http.*

interface TodoListAPI {
    @GET("list")
    suspend fun getTodoList(): Response<ResponseListDTO>

    @POST("list")
    suspend fun addTodoItem(
        @Body requestDTO: RequestDTO,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<ResponseItemDTO>


    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Path("id") todoId: String,
        @Body requestDTO: RequestDTO,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<ResponseItemDTO>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Path("id") todoId: String,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<ResponseItemDTO>


    @GET("list/{id}")
    suspend fun getTodoItem(
        @Path("id") todoId: String
    ): Response<ResponseItemDTO>

}