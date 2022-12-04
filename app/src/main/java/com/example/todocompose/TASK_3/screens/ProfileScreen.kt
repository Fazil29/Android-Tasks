package com.example.todocompose.TASK_3.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.todocompose.R
import com.example.todocompose.TASK_3.util.UserModel
import com.example.todocompose.TASK_3.view_models.ProfileViewModel

@Composable
fun ProfileScreen(activity: Activity, user: UserModel, profileViewModel: ProfileViewModel) {

    val uri by profileViewModel.profilePhotoUri.collectAsState()
    profileViewModel.getPhotoURI()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
           it?.let { profileViewModel.saveToCloud(activity, it) }
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (profilePicIV, firstNameTV, lastNameTV, emailTV) = createRefs()

        AsyncImage(model = uri,
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
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

        OutlinedTextField(value = user.firstName ?: "FirstName",
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

        OutlinedTextField(value = user.lastName ?: "LastName",
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

        OutlinedTextField(value = user.email ?: "Nothing",
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