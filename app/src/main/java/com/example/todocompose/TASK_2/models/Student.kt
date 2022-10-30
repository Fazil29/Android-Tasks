package com.example.todocompose.TASK_2.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Student(
    val firstName: String?,
    val lastName: String?,
    val standard: String?,
    val id: String? = UUID.randomUUID().toString()
): Parcelable