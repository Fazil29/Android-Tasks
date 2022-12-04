package com.example.todocompose.TASK_3.util.helpers

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.tasks.await

object SignInHelper{

    suspend fun openDialog(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>, signIn: Boolean){
        val oneTapClient = Identity.getSignInClient(context)
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("553104867016-4a5rffksa0vhvt2j4vlvv8650biepqte.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(signIn)
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

}