package com.example.todocompose.TASK_3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todocompose.TASK_3.screens.DetailScreen
import com.example.todocompose.TASK_3.screens.SignInScreen
import com.example.todocompose.TASK_3.ui.theme.ToDoComposeTheme
import com.example.todocompose.TASK_3.util.Auth
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Auth.oneTapClient = Identity.getSignInClient(this@MainActivity)
        Auth.oneTapResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){
            try {
                val credential = Auth.oneTapClient!!.getSignInCredentialFromIntent(it.data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        val msg = "idToken: $idToken"
//                Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).show()
                        Log.d("one tap", msg)
                    }
                    else -> {
                        // Shouldn't happen.
                        Log.d("one tap", "No ID token!")
//                Snackbar.make(binding.root, "No ID token!", Snackbar.LENGTH_INDEFINITE).show()
                    }
                }
            }
            catch (e: ApiException) {
                println(e)
            }
        }
        setContent {
            ToDoComposeTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = "signin_screen"){
                    composable(route = "signin_screen"){
                        SignInScreen(this@MainActivity)
                    }
                    composable(route = "detail_screen"){
                        DetailScreen()
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