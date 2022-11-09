package com.example.todocompose.TASK_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todocompose.TASK_3.screens.LoginScreen
import com.example.todocompose.TASK_3.screens.ProfileScreen
import com.example.todocompose.TASK_3.ui.theme.ToDoComposeTheme
import com.example.todocompose.TASK_3.util.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoComposeTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Login.route){
                    composable(route = Screen.Login.route){
                        LoginScreen(navController)
                    }
                    composable(route = Screen.Profile.route){
                        ProfileScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    ToDoComposeTheme {
        Greeting2("Android")
    }
}