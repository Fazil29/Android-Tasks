package com.example.todocompose.TASK_1.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todocompose.TASK_1.ui.screens.Screen1
import com.example.todocompose.TASK_1.ui.screens.Screen2
import com.example.todocompose.TASK_1.ui.screens.Screen3
import com.example.todocompose.TASK_1.ui.screens.Screen4
import com.example.todocompose.TASK_1.ui.ui.theme.ToDoComposeTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoComposeTheme{
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "screen1"){
                    composable(route = "screen1"){
                        Screen1(navController)
                    }
                    composable(route = "screen2"){
                        Screen2(navController)
                    }
                    composable(route = "screen3"){
                        Screen3(navController)
                    }
                    composable(route = "screen4"){
                        Screen4(navController)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    ToDoComposeTheme {
        Screen1(navController = rememberNavController())
    }
}