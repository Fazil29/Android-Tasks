package com.example.todocompose.TASK_2

import HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todocompose.TASK_2.models.Student
import com.example.todocompose.TASK_2.screens.DisplayScreen
import com.example.todocompose.TASK_2.ui.theme.ToDoComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoComposeTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home_screen") {
                    composable(route = "home_screen") {
                        HomeScreen(navController)
                    }
                    composable(
                        route = "display_screen/{first_name}/{last_name}/{standard}",
                        arguments = listOf(
                            navArgument(
                                name = "first_name"
                            ) {
                                type = NavType.StringType
                            },
                            navArgument(
                                name = "last_name"
                            ) {
                                type = NavType.StringType
                            },
                            navArgument(
                                name = "standard"
                            ) {
                                type = NavType.StringType
                            })
                    ) {
                        val student: Student = Student(it.arguments?.getString("first_name").toString(), it.arguments?.getString("last_name").toString(), it.arguments?.getString("standard").toString())
                        DisplayScreen(student = student)
                    }
                }
            }
        }
    }
}