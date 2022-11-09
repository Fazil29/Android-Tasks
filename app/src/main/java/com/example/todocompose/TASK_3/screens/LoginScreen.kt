package com.example.todocompose.TASK_3.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.todocompose.TASK_3.util.Auth
import com.example.todocompose.TASK_3.util.Screen
import com.example.todocompose.TASK_3.util.UserModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current as Activity
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()){ result ->
        if (result.resultCode != Activity.RESULT_OK) {
            return@rememberLauncherForActivityResult
        }
        val oneTapClient = Identity.getSignInClient(context)
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken
        if (idToken != null) {
            credential.let {
                Auth.user = UserModel(it.givenName, it.familyName, it.id, it.profilePictureUri)
                navController.navigate(Screen.Profile.route)
            }
        } else {
            Log.d("LOG", "Null Token")
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (signInButton, signUpButton) = createRefs()
            Button(onClick = { scope.launch { openSignInDialog(context, launcher) } },
                modifier = Modifier.constrainAs(signInButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(signUpButton.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Text(text = "Sign In With Google")
            }
            Button(onClick = { scope.launch { openSignUpDialog(context, launcher) } },
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



suspend fun openSignInDialog(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>){
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId("553104867016-4a5rffksa0vhvt2j4vlvv8650biepqte.apps.googleusercontent.com")
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        // Automatically sign in when exactly one credential is retrieved.
        .setAutoSelectEnabled(true)
        .build()

    try {
        // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
        // Instead of listeners that aren't cleaned up automatically
        val result = oneTapClient.beginSignIn(signInRequest).await()

        // Now construct the IntentSenderRequest the launcher requires
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)
    } catch (e: Exception) {
        // No saved credentials found. Launch the One Tap sign-up flow, or
        // do nothing and continue presenting the signed-out UI.
        openSignUpDialog(context, launcher)
        Log.d("LOG Error", e.message.toString())
    }
}

suspend fun openSignUpDialog(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>){
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("553104867016-4a5rffksa0vhvt2j4vlvv8650biepqte.apps.googleusercontent.com")
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    try {
        val result = oneTapClient.beginSignIn(signInRequest).await()
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)
    } catch (e: Exception) {
        Log.d("LOG Error", e.message.toString())
    }
}
