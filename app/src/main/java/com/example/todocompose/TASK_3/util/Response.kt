package com.example.todocompose.TASK_3.util

sealed class Response<T>(val data: T? = null, val message: String? = null){
    class LOADING<T>(): Response<T>()
    class SUCCESS<T>(data: T): Response<T>(data = data)
    class ERROR<T>(message: String): Response<T>(message = message)
}
