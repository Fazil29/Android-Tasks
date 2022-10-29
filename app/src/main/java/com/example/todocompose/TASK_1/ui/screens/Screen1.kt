package com.example.todocompose.TASK_1.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Screen1(navController: NavController) {
    Scaffold(
        topBar = { GetAppBar(screenNo = "1") }
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (pageNumberTV, nextPageBTN) = createRefs()

            Text(text = "Screen 1",
                modifier = Modifier.constrainAs(pageNumberTV) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(nextPageBTN.top)
                })

            Button(onClick = { navController.navigate("screen2") },
                modifier = Modifier.constrainAs(nextPageBTN) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(pageNumberTV.bottom)
                    bottom.linkTo(parent.bottom)
                }) {
                Text(text = "Go To Screen 2")
            }
        }
    }
}

@Composable
private fun GetAppBar(screenNo: String) {
    TopAppBar(title = {
        Text(text = "Screen $screenNo")
    })
}