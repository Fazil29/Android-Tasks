package com.example.todocompose.TASK_3.util

sealed class Screen(val route: String){
    object Login: Screen("login_screen")
    object Profile: Screen("profile_screen")
}
