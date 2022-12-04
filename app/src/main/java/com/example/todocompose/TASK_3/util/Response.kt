package com.example.todocompose.TASK_3.util

sealed class Response<T>{
    class LOADING<T>: Response<T>()
    class SUCCESS<T>: Response<T>()
    class ERROR<T>: Response<T>()
}
