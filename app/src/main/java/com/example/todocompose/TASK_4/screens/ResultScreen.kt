package com.example.todocompose.TASK_4.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.todocompose.TASK_4.util.ServiceLocator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ResultScreen() {
    Scaffold() {
        val bitmap by remember {
            mutableStateOf<Bitmap?>(ServiceLocator.imageBitmap)
        }

//        val bitmap = remember {
//            mutableStateOf<Bitmap?>(ServiceLocator.imageBitmap)
//        }

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val imageIV = createRef()
            Image(bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(imageIV) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}