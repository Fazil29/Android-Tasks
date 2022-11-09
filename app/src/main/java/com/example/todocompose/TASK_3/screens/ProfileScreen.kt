package com.example.todocompose.TASK_3.screens

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.todocompose.TASK_3.util.Auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

@Composable
fun ProfileScreen() {
    val activity = LocalContext.current as Activity

    var uri by remember {
        mutableStateOf(Auth.user!!.profilePicture)
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        uri = saveToCloud(activity, it)
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (profilePicIV, firstNameTV, lastNameTV, emailTV) = createRefs()

        AsyncImage(model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .constrainAs(profilePicIV) {
                    top.linkTo(parent.top)
                    bottom.linkTo(firstNameTV.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .clickable { launcher.launch("image/*") }
        )

        OutlinedTextField(value = Auth.user!!.firstName?:"Nothing",
            enabled = false,
            onValueChange = {},
            label = {
                Text(text = "First Name")
            },
            modifier = Modifier.constrainAs(firstNameTV) {
                top.linkTo(profilePicIV.bottom)
                bottom.linkTo(lastNameTV.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        OutlinedTextField(value = Auth.user!!.lastName?:"Nothing",
            enabled = false,
            onValueChange = {},
            label = {
                Text(text = "Last Name")
            },
            modifier = Modifier.constrainAs(lastNameTV) {
                top.linkTo(firstNameTV.bottom)
                bottom.linkTo(emailTV.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        OutlinedTextField(value = Auth.user!!.email?:"Nothing",
            enabled = false,
            onValueChange = {},
            label = {
                Text(text = "Email")
            },
            modifier = Modifier.constrainAs(emailTV) {
                top.linkTo(lastNameTV.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
    }
}

fun saveToCloud(context: Activity, uri: Uri?):Uri? {

    var resultUri: Uri? = uri

    val bitmap = if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            resultUri
        )
    } else {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                context.contentResolver,
                resultUri!!
            )
        )
    }

    Toast.makeText(context, "Saving", Toast.LENGTH_SHORT).show()

    val imageReference = FirebaseStorage.getInstance().reference.child("${UUID.randomUUID()}.jpg")
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

    val image = outputStream.toByteArray()

    imageReference.putBytes(image).addOnFailureListener {
        Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
    }.addOnSuccessListener {
        Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()
        resultUri = it.uploadSessionUri
        saveNewPhotoLink(resultUri.toString())
    }

    return resultUri
}

fun saveNewPhotoLink(newProfilePicLink: String){
    val database = FirebaseFirestore.getInstance()
    val user = hashMapOf(
        Auth.user!!.email to newProfilePicLink,
    )

    database.collection("User_Collection")
        .add(user)
        .addOnSuccessListener { documentReference ->
            Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("TAG", "Error adding document", e)
        }
}