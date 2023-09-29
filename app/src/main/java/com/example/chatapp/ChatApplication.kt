package com.example.chatapp

import android.app.Application

class ChatApplication : Application() {

    val appComponent: ApplicationComponent = DaggerApplicationComponent.create()
}