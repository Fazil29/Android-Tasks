package com.example.todocompose.TASK_3.util

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserModel(
    val firstName: String,
    val lastName: String,
    val email: String,
)
