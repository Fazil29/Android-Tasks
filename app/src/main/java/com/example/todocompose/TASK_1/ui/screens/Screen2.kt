package com.example.todocompose.TASK_1.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Screen2(navController: NavController) {
    Scaffold(
        topBar = { GetAppBar(screenNo = "2") }
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (pageNumberTV, nextPageBTN, previousPageBTN) = createRefs()

            Text(text = "Screen 2",
                modifier = Modifier.constrainAs(pageNumberTV) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(nextPageBTN.top)
                })

            Button(onClick = { navController.navigate("screen3") },
                modifier = Modifier.constrainAs(nextPageBTN) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(pageNumberTV.bottom)
                    bottom.linkTo(previousPageBTN.top)
                }) {
                Text(text = "Go To Screen 3")
            }

            OutlinedButton(onClick = { navController.popBackStack() },
                modifier = Modifier.constrainAs(previousPageBTN) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(nextPageBTN.bottom)
                    bottom.linkTo(parent.bottom)
                }) {
                Text(text = "Back")
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