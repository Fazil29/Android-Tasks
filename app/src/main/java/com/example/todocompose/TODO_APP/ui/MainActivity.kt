package com.example.todocompose.TODO_APP.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.todocompose.R
import com.example.todocompose.TODO_APP.data.models.Todo
import com.example.todocompose.TODO_APP.ui.ui.theme.ToDoComposeTheme
import com.example.todocompose.TODO_APP.viewmodel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val todoViewModel: TodoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoComposeTheme {
                Scaffold(
                    topBar = { GetAppBar() },
                    floatingActionButton = { GetFloatingButton() },
                ){
                    it.calculateBottomPadding()
                }
            }
        }
    }

    private fun insert(title: MutableState<String>, description: MutableState<String>) {
        if (!TextUtils.isEmpty(title.value) && !TextUtils.isEmpty(description.value)) {
            lifecycleScope.launchWhenStarted {
                todoViewModel.addTodo(Todo(title.value, description.value))
            }
            Toast.makeText(this, "Successfully Inserted", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "Enter Something", Toast.LENGTH_SHORT).show()
    }

    @Composable
    private fun GetAppBar() {
        TopAppBar(
            title = {
                Text(text = "Welcome")
            },
        )
    }

    @Composable
    private fun GetFloatingButton() {
        val openDialog = remember { mutableStateOf(false) }
        FloatingActionButton(onClick = { openDialog.value = true }) {
            OpenDialogBox(openDialog = openDialog)
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
        }
    }

    @Composable
    private fun OpenDialogBox(openDialog: MutableState<Boolean>) {

        val title = remember { mutableStateOf("") }
        val content = remember { mutableStateOf("") }
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = "Add ToDo")
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = title.value,
                            onValueChange = { title.value = it },
                            placeholder = { Text(text = "Enter Title") },
                            label = { Text(text = "Title") },
                        )
                        OutlinedTextField(
                            value = content.value,
                            onValueChange = { content.value = it },
                            placeholder = { Text(text = "Enter Description") },
                            label = { Text(text = "Description") },
                        )
                    }
                },
                confirmButton = {
                    OutlinedButton(onClick = { insert(title, content)
                    openDialog.value = false
                    }) {
                        Text(text = "Save")
                    }
                }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
//    ToDoComposeTheme {
//        Greeting2("Android")
//    }
}