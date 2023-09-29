package com.example.chatapp

import android.content.Context
import com.example.chatapp.data.DataModule
import com.example.chatapp.presentation.MessagesActivity
import com.example.chatapp.presentation.viewModel.MessagesViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Component(
    modules = [AppModule::class, DataModule::class]
)
interface ApplicationComponent {

    val messagesViewModelFactory: MessagesViewModelFactory

    fun inject(messagesActivity: MessagesActivity)
}


val Context.applicationComponent
    get() = (applicationContext as ChatApplication).appComponent

@Module
object AppModule {

    @Provides
    fun provideCoroutineDispatcher() = Dispatchers.Default
}