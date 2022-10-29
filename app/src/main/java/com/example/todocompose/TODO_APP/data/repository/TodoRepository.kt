package com.example.todocompose.TODO_APP.data.repository

import com.example.todocompose.TODO_APP.data.local.TodoDAO
import com.example.todocompose.TODO_APP.data.models.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDAO: TodoDAO) {

    suspend fun insertTodo(todo: Todo){
        withContext(Dispatchers.IO){
            todoDAO.addToDo(todo)
        }
    }

    suspend fun getTodos(): Flow<List<Todo>>{
        return todoDAO.getToDos()
    }
}