package com.example.todocompose.TASK_2.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.todocompose.TASK_2.models.Student

@Composable
fun DisplayScreen(student: Student) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val(firstNameTV, lastNameTV, standardTV, idTV) = createRefs()

        Text(text = "First Name: ${student.firstName}",
        modifier = Modifier.constrainAs(firstNameTV){
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(lastNameTV.top)
        })

        Text(text = "Last Name: ${student.lastName}",
            modifier = Modifier.constrainAs(lastNameTV){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(firstNameTV.bottom)
                bottom.linkTo(standardTV.top)
            })

        Text(text = "Standard: ${student.standard}",
            modifier = Modifier.constrainAs(standardTV){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(lastNameTV.bottom)
                bottom.linkTo(idTV.top)
            })

        Text(text = "Id: ${student.id}",
            modifier = Modifier.constrainAs(idTV){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(standardTV.bottom)
                bottom.linkTo(parent.bottom)
            })
    }
}