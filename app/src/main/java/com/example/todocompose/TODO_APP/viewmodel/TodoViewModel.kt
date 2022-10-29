package com.example.todocompose.TODO_APP.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocompose.TODO_APP.data.models.Todo
import com.example.todocompose.TODO_APP.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val todoRepository: TodoRepository): ViewModel() {

    val response: MutableState<List<Todo>> = mutableStateOf(listOf<Todo>())

    init {
        getAllTodos()
    }

     fun getAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.getTodos()
                .catch {
                    Log.d("ERROR VM", "${it.message}")
                }
                .collect{
                    response.value = it
                }
        }
    }

     fun addTodo(todo: Todo){
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.insertTodo(todo)
        }
    }
}