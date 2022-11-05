package com.example.todocompose.TASK_3.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.todocompose.TASK_3.util.Auth
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider.getCredential

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignInScreen(context: Activity) {
    Scaffold {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (signInButton, signUpButton) = createRefs()
            Button(onClick = { openSignInDialog(context) },
                modifier = Modifier.constrainAs(signInButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(signUpButton.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Text(text = "Sign In With Google")
            }
            Button(onClick = { openSignUpDialog(context) },
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



fun openSignInDialog(context: Activity){
    Auth.oneTapClient!!.beginSignIn(Auth.provideSignInRequest)
        .addOnSuccessListener(context) {
            val ib = IntentSenderRequest.Builder(it.pendingIntent.intentSender).build()
            Auth.oneTapResult!!.launch(ib)
        }
        .addOnFailureListener(context){
            openSignUpDialog(context)
        }
}

fun openSignUpDialog(context: Activity){
    Auth.oneTapClient!!.beginSignIn(Auth.provideSignUpRequest)
        .addOnSuccessListener(context) {
            val ib = IntentSenderRequest.Builder(it.pendingIntent.intentSender).build()
            Auth.oneTapResult!!.launch(ib)
        }
        .addOnFailureListener(context){
            openSignUpDialog(context)
        }
}