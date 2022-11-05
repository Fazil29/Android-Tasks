package com.example.todocompose.TASK_4.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.todocompose.TASK_4.util.ServiceLocator
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PickerScreen(activity: Activity, navController: NavController) {
    Scaffold() {

        var imageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        var bitmap by remember {
            mutableStateOf<Bitmap?>(null)
        }
        val launcher = rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            imageUri = uri
        }

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (pickBTN, submitBTN) = createRefs()
            Button(onClick = { launcher.launch("image/*") },
                modifier = Modifier.constrainAs(pickBTN) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                }) {
                Text(text = "Pick")
            }
            if (imageUri != null) {
                bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(
                        activity.contentResolver,
                        imageUri
                    )
                } else {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            activity.contentResolver,
                            imageUri!!
                        )
                    )
                }

                Button(onClick = { saveToCloudAndShow(bitmap!!, activity, navController) },
                    modifier = Modifier.constrainAs(submitBTN) {
                        top.linkTo(pickBTN.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                    Text(text = "Submit")
                }
            }
        }
    }
}

fun saveToCloudAndShow(bitmap: Bitmap, context: Activity, navController: NavController) {
    Toast.makeText(context, "Uploading", Toast.LENGTH_SHORT).show()
    val imageReference = FirebaseStorage.getInstance().reference.child("${UUID.randomUUID()}.jpg")
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val image = outputStream.toByteArray()

    imageReference.putBytes(image).addOnFailureListener {
        Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
    }.addOnSuccessListener {
        Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()
        ServiceLocator.imageBitmap = bitmap
        navController.navigate("result_screen")
    }
}