//package com.example.todolist
//
//import android.content.Context
//import com.example.todolist.data.datasource.AppDataBase
//import com.example.todolist.presentation.MainActivity
//import dagger.BindsInstance
//import dagger.Component
//import javax.inject.Singleton
//
//@Singleton
//@Component(
//    modules = [AppModule::class]
//)
//interface AppComponent {
//
//    val database: AppDataBase
//    val context: Context
//
//    fun inject(mainActivity: MainActivity)
//    fun inject(application: App)
//
//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        fun context(context: Context): Builder
//
//        fun build(): AppComponent
//    }
//}