package com.example.todocompose.TASK_3.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Credentials
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.net.toUri
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

object Helpers {

     suspend fun getCredentialsFromLocal(context: Context): UserModel {
        context.apply {
            val firstName = readString(FIRST_NAME).first()
            val lastName = readString(LAST_NAME).first()
            val email = readString(EMAIL_ID).first()
            return UserModel(firstName, lastName, email)
        }
    }

    fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
        } else {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    uri
                )
            )
        }
    }

    fun sessionToLinkURI(session: String): Uri = "${session.substring(0, session.indexOf("&uploadType"))}&alt=media".toUri()

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

    suspend fun saveCredentials(context: Context, credentials: SignInCredential){
        context.apply {
            writeBool(LOGGED_IN, true)
            writeString(FIRST_NAME, credentials.givenName?:"FirstName")
            writeString(LAST_NAME, credentials.familyName?:"LastName")
            writeString(EMAIL_ID, credentials.id)
        }
    }

    suspend fun saveNewPhotoLink(email: String, newProfilePicLink: String) {
        val database = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            email to newProfilePicLink,
        )
        try {
            database.collection("User_Collection")
                .document("ProfilePictureMapping")
                .set(user, SetOptions.merge())
                .await()
            Log.d("TAG", "DocumentSnapshot added")
        } catch (e: Exception) {
            Log.w("TAG", "Error adding document", e)
        }
    }

    suspend fun getProfilePicURI(email: String): Uri{
        val database = FirebaseFirestore.getInstance()
        val doc = database.collection("User_Collection")
            .document("ProfilePictureMapping")
            .get()
            .await()
        return doc.data?.get(email).toString().toUri()
    }
}