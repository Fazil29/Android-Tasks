package com.example.todocompose.TASK_3.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.todocompose.TASK_3.util.*
import com.example.todocompose.TASK_3.view_models.ProfileViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController, context: Context, profileViewModel: ProfileViewModel) {

    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            scope.launch {
                profileViewModel.logInWithGoogle(it, context) {
                    navController.navigate(Screen.Profile.route) {
                        this.popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }

    Scaffold {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (signInButton, signUpButton) = createRefs()
            Button(onClick = {
                scope.launch {
                    profileViewModel.openSignInDialog(
                        context,
                        launcher
                    )
                }
            },
                modifier = Modifier.constrainAs(signInButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(signUpButton.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Text(text = "Sign In With Google")
            }
            Button(onClick = {
                scope.launch {
                    profileViewModel.openSignUpDialog(
                        context,
                        launcher
                    )
                }
            },
                modifier = Modifier.constrainAs(signUpButton) {
                    top.linkTo(signInButton.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Text(text = "Sign Up With Google")
            }
        }
    }
}
