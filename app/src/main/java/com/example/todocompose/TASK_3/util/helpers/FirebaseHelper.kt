package com.example.todocompose.TASK_3.util.helpers

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseHelper {

    suspend fun saveNewPhotoLink(email: String, newProfilePicLink: String) {
        val database = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            email to newProfilePicLink,
        )
        try {
            database.collection("User_Collection")
                .document(email)
                .set(user)
                .await()
            Log.d("TAG", "DocumentSnapshot added")
        } catch (e: Exception) {
            Log.w("TAG", "Error adding document", e)
        }
    }

    suspend fun getProfilePicURI(email: String): Uri {
        val database = FirebaseFirestore.getInstance()
        val doc = database.collection("User_Collection")
            .document(email)
            .get()
            .await()
        return doc.data?.get(email).toString().toUri()
    }

}