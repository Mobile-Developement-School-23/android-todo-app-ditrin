//package com.example.todolist
//
//import android.app.Application
//import android.content.Context
//import com.example.todolist.data.datasource.AppDataBase
//import com.example.todolist.data.datasource.TodoListDao
//import com.example.todolist.data.model.RequestDTO
//import dagger.Module
//import dagger.Provides
//import javax.inject.Singleton
//
//@Module
//class AppModule {
//
//    @Singleton
//    @Provides
//    fun provideAppDatabase(context: Context): AppDataBase {
//        return AppDataBase.getInstance(context as Application)
//    }
//
//    @Singleton
//    @Provides
//    fun provideTodoDao(appDatabase: AppDataBase): TodoListDao {
//        return appDatabase.todoListDao()
//    }
//}