package com.example.todocompose.TODO_APP.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    val Title: String,
    val Content: String?
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
