package com.example.todolist

import android.app.Application
import android.content.Context

//class App : Application() {
//
//    private var _appComponent: AppComponent? = null
//    val appComponent: AppComponent
//        get() = requireNotNull(_appComponent) {
//            "AppComponent must be not null"
//        }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        _appComponent = DaggerAppComponent.builder()
//            .context(this)
//            .build()
//
//        appComponent.inject(this)
//    }
//
//}
//
//val Context.appComponent: AppComponent
//    get() = when (this) {
//        is App -> appComponent
//        else -> (applicationContext as App).appComponent
//    }