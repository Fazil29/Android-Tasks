package com.example.todocompose.TODO_APP.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todocompose.TODO_APP.data.models.Todo

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun getDao(): TodoDAO
}