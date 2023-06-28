package com.katerina.todoapp

import android.app.Application
import com.katerina.todoapp.di.AppComponent
import com.katerina.todoapp.di.DaggerAppComponent

class ToDoApp : Application() {
    val appComponent: AppComponent = DaggerAppComponent.factory().create(this)
}