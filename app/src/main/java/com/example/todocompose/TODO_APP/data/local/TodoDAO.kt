package com.example.todocompose.TODO_APP.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todocompose.TODO_APP.data.models.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToDo(todo: Todo)

    @Query("SELECT * FROM todo_table")
    fun getToDos(): Flow<List<Todo>>
}