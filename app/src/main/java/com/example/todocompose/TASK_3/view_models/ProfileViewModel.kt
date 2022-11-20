package com.example.todocompose.TASK_3.view_models

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.example.todocompose.TASK_3.util.*
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.rpc.Help
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

class ProfileViewModel : ViewModel() {

    var user: UserModel? = null
        private set

    private val _loggedInStatus = MutableStateFlow<Response<Boolean>>(Response.LOADING())
    val loggedInStatus: StateFlow<Response<Boolean>> = _loggedInStatus

    private val _profilePhotoUri = MutableStateFlow<Uri?>(null)
    val profilePhotoUri: StateFlow<Uri?> = _profilePhotoUri


    fun isLoggedIn(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loggedInStatus.value = Response.SUCCESS(context.readBool(LOGGED_IN).first())
                user = Helpers.getCredentialsFromLocal(context)
            } catch (e: Exception) {
                _loggedInStatus.value = Response.ERROR("ERROR: ${e.message}")
            }
        }
    }


    suspend fun logInWithGoogle(result: ActivityResult, context: Context, navigate: () -> Unit) {
        if (result.resultCode != Activity.RESULT_OK) {
            return
        }

        val oneTapClient = Identity.getSignInClient(context)
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        val idToken = credential.googleIdToken

        if (idToken != null) {
            viewModelScope.launch(Dispatchers.IO) {
                Helpers.saveCredentials(context, credential)
            }
            this@ProfileViewModel.user =
                UserModel(credential.givenName, credential.familyName, credential.id)
            if (Helpers.getProfilePicURI(user!!.email!!) == "null".toUri())
                Helpers.saveNewPhotoLink(user!!.email!!, credential.profilePictureUri.toString())
            navigate()
        } else {
            Log.d("LOG", "Null Token")
        }
    }


    suspend fun openSignInDialog(
        context: Context,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        Helpers.openDialog(context, launcher, true)
    }


    suspend fun openSignUpDialog(
        context: Context,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        Helpers.openDialog(context, launcher, false)
    }


    fun saveToCloud(context: Activity, uri: Uri) {
        var resultUri: Uri = uri
        _profilePhotoUri.value = resultUri
        val bitmap = Helpers.uriToBitmap(context, resultUri)

        Toast.makeText(context, "Saving", Toast.LENGTH_SHORT).show()

        val imageReference =
            FirebaseStorage.getInstance().reference.child("${UUID.randomUUID()}.jpg")
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val image = outputStream.toByteArray()

        try {
            viewModelScope.launch(Dispatchers.IO) {
                imageReference.putBytes(image).await().apply {
                    resultUri = Helpers.sessionToLinkURI(uploadSessionUri.toString())
                }
                Helpers.saveNewPhotoLink(user!!.email!!, resultUri.toString())
                _profilePhotoUri.value = resultUri
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
        }
    }


    fun getPhotoURI() {
        viewModelScope.launch(Dispatchers.IO) {
            _profilePhotoUri.value = Helpers.getProfilePicURI(user!!.email!!)
        }
    }
}