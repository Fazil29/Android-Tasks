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
fun Screen4(navController: NavController) {
    Scaffold(
        topBar = { GetAppBar(screenNo = "4") }
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (pageNumberTV, nextPageBTN) = createRefs()

            Text(text = "Screen 4",
                modifier = Modifier.constrainAs(pageNumberTV) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(nextPageBTN.top)
                })

            OutlinedButton(onClick = { navController.popBackStack() },
                modifier = Modifier.constrainAs(nextPageBTN) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(pageNumberTV.bottom)
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