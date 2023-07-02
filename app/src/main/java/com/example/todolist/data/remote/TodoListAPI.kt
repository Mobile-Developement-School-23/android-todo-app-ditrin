package com.example.todolist.data.remote

import com.example.todolist.data.model.RequestDTO
import com.example.todolist.data.model.ResponseItemDTO
import com.example.todolist.data.model.ResponseListDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoListAPI {
    @GET("list")
    suspend fun getTodoList(): ResponseListDTO

    @POST("list")
    suspend fun addTodoItem(
        @Body requestDTO: RequestDTO,
        @Header("X-Last-Known-Revision") revision: Int
    ): ResponseItemDTO


    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Path("id") todoId: String,
        @Body requestDTO: RequestDTO,
        @Header("X-Last-Known-Revision") revision: Int
    ): ResponseItemDTO

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Path("id") todoId: String,
        @Header("X-Last-Known-Revision") revision: Int
    ): ResponseItemDTO
}