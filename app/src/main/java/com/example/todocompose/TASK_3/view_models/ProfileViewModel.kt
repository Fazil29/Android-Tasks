package com.example.todocompose.TASK_3.view_models

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocompose.TASK_3.util.Response
import com.example.todocompose.TASK_3.util.UserModel
import com.example.todocompose.TASK_3.util.helpers.ConversionHelper
import com.example.todocompose.TASK_3.util.helpers.CredentialHelper
import com.example.todocompose.TASK_3.util.helpers.FirebaseHelper
import com.example.todocompose.TASK_3.util.helpers.SignInHelper
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

class ProfileViewModel : ViewModel() {

    var user: UserModel = UserModel("", "", "")
        private set

    private val _loggedInStatus = MutableStateFlow<Response<UserModel>>(Response.LOADING())
    val loggedInStatus: StateFlow<Response<UserModel>> = _loggedInStatus

    private val _profilePhotoUri = MutableStateFlow<Uri?>(null)
    val profilePhotoUri: StateFlow<Uri?> = _profilePhotoUri

    fun isLoggedIn(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                CredentialHelper.getCredentialsFromLocal(context)?.let {
                    user = it
                    _loggedInStatus.value = Response.SUCCESS()
                }?: run {
                    _loggedInStatus.value = Response.ERROR()
                }
            } catch (e: Exception) {
                _loggedInStatus.value = Response.ERROR()
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
                CredentialHelper.saveCredentials(context, credential)
            }
            this@ProfileViewModel.user =
                UserModel(credential.givenName.toString(), credential.familyName.toString(), credential.id)
            if (FirebaseHelper.getProfilePicURI(user.email) == "null".toUri())
                FirebaseHelper.saveNewPhotoLink(user.email, credential.profilePictureUri.toString())
            navigate()
        } else {
            Log.d("LOG", "Null Token")
        }
    }


    suspend fun openSignInDialog(
        context: Context,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        SignInHelper.openDialog(context, launcher, true)
    }


    suspend fun openSignUpDialog(
        context: Context,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        SignInHelper.openDialog(context, launcher, false)
    }


    fun saveToCloud(context: Activity, uri: Uri) {
        var resultUri: Uri = uri
        _profilePhotoUri.value = resultUri
        val bitmap = ConversionHelper.uriToBitmap(context, resultUri)

        Toast.makeText(context, "Saving", Toast.LENGTH_SHORT).show()

        val imageReference =
            FirebaseStorage.getInstance().reference.child("${UUID.randomUUID()}.jpg")
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val image = outputStream.toByteArray()

        try {
            viewModelScope.launch(Dispatchers.IO) {
                imageReference.putBytes(image).await().apply {
                    resultUri = ConversionHelper.sessionToLinkURI(uploadSessionUri.toString())
                }
                FirebaseHelper.saveNewPhotoLink(user.email, resultUri.toString())
                _profilePhotoUri.value = resultUri
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
        }
    }


    fun getPhotoURI() {
        viewModelScope.launch(Dispatchers.IO) {
            _profilePhotoUri.value = FirebaseHelper.getProfilePicURI(user.email)
        }
    }
}