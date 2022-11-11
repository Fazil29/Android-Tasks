package com.example.todocompose.TASK_3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todocompose.TASK_3.screens.LoginScreen
import com.example.todocompose.TASK_3.screens.ProfileScreen
import com.example.todocompose.TASK_3.ui.theme.ToDoComposeTheme
import com.example.todocompose.TASK_3.util.*
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlin.properties.Delegates

class MainActivity : ComponentActivity() {
    private var isLoggedIn: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            runBlocking {
                isLoggedIn = this@MainActivity.readBool(LOGGED_IN).first()
                if(isLoggedIn){
                    this@MainActivity.apply {
                        val firstName = readString(FIRST_NAME).first()
                        val lastName = readString(LAST_NAME).first()
                        val email = readString(EMAIL_ID).first()
                        val docId = readString(DOC_ID).first()
                        Auth.user = UserModel(firstName, lastName, email, null)
                        Auth.docId = docId
                    }
                }
            }
            ToDoComposeTheme {
                val navController: NavHostController = rememberNavController()
                Log.d("CHECK", isLoggedIn.toString())
                val startingDestination: String = if (isLoggedIn) Screen.Profile.route else Screen.Login.route
                NavHost(
                    navController = navController,
                    startDestination = startingDestination
                ) {
                    composable(route = Screen.Login.route) {
                        LoginScreen(navController)
                    }
                    composable(route = Screen.Profile.route) {
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