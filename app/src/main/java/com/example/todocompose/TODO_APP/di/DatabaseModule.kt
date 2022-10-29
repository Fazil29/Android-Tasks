package com.example.todocompose.TODO_APP.di

import android.app.Application
import androidx.room.Room
import com.example.todocompose.TODO_APP.data.local.TodoDAO
import com.example.todocompose.TODO_APP.data.local.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(application: Application): TodoDatabase{
        return Room
            .databaseBuilder(application, TodoDatabase::class.java, "TodoDatabase")
            .build()
    }

    @Provides
    @Singleton
    fun providesTodoDao(database: TodoDatabase): TodoDAO{
        return database.getDao()
    }
}