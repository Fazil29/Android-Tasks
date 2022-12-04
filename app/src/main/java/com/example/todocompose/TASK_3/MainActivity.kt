package com.example.todocompose.TASK_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todocompose.TASK_3.screens.LoginScreen
import com.example.todocompose.TASK_3.screens.ProfileScreen
import com.example.todocompose.TASK_3.ui.theme.ToDoComposeTheme
import com.example.todocompose.TASK_3.util.Response
import com.example.todocompose.TASK_3.util.Screen
import com.example.todocompose.TASK_3.view_models.ProfileViewModel

class MainActivity : ComponentActivity() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            profileViewModel = viewModel()
            profileViewModel.isLoggedIn(this)

            val isLoggedIn by profileViewModel.loggedInStatus.collectAsState()
            var startDestination: String = ""

            ToDoComposeTheme {
                val navController: NavHostController = rememberNavController()
                when (isLoggedIn) {

                    is Response.LOADING -> {
                        Surface(modifier = Modifier.fillMaxSize(), color = Color.Cyan) {
                            CircularProgressIndicator(modifier = Modifier.size(100.dp))
                        }
                    }

                    is Response.SUCCESS -> {
                        startDestination = Screen.Profile.route
                    }

                    is Response.ERROR -> {
                        startDestination = Screen.Login.route
                    }
                }

                if(startDestination.isNotEmpty()){
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(route = Screen.Login.route) {
                            LoginScreen(navController, this@MainActivity, profileViewModel)
                        }
                        composable(route = Screen.Profile.route) {
                            ProfileScreen(
                                this@MainActivity, profileViewModel.user, profileViewModel
                            )
                        }
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