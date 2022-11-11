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
import com.example.todocompose.TASK_3.util.*
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController) {

    val myScope = rememberCoroutineScope()
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
                myScope.launch {
                    context.apply {
                        writeBool(LOGGED_IN, true)
                        writeString(FIRST_NAME, it.givenName?:"FirstName")
                        writeString(LAST_NAME, it.familyName?:"LastName")
                        writeString(EMAIL_ID, it.id)
                    }
                }
                Auth.user = UserModel(it.givenName, it.familyName, it.id, it.profilePictureUri)
                navController.navigate(Screen.Profile.route){
                    this.popUpTo(Screen.Login.route){
                        inclusive = true
                    }
                }
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
                .setServerClientId("553104867016-4a5rffksa0vhvt2j4vlvv8650biepqte.apps.googleusercontent.com")
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    try {
        val result = oneTapClient.beginSignIn(signInRequest).await()
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)
    } catch (e: Exception) {
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
