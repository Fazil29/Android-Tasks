import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
//@Preview(showBackground = true)
//fun HomeScreen() {
fun HomeScreen(navController: NavController) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (firstNameTF, lastNameTF, standardTF, submitBTN) = createRefs()

        val firstName: MutableState<String> = remember {
            mutableStateOf("")
        }

        val lastName: MutableState<String> = remember {
            mutableStateOf("")
        }

        val standard: MutableState<String> = remember {
            mutableStateOf("")
        }

        OutlinedTextField(
            value = firstName.value,
            onValueChange = {
                firstName.value = it
            },
            label = {
                Text(text = "First Name")
            },
            modifier = Modifier.constrainAs(firstNameTF) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(lastNameTF.top)
            }
        )

        OutlinedTextField(value = lastName.value,
            onValueChange = {
                lastName.value = it
            },
            label = {
                Text(text = "Last Name")
            },
            modifier = Modifier.constrainAs(lastNameTF) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(firstNameTF.bottom)
                bottom.linkTo(standardTF.top)
            }
        )

        OutlinedTextField(value = standard.value,
            onValueChange = {
                standard.value = it
            },
            label = {
                Text(text = "Standard")
            },
            modifier = Modifier.constrainAs(standardTF) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(lastNameTF.bottom)
                bottom.linkTo(submitBTN.top)
            }
        )

        Button(onClick = { navController.navigate("display_screen/${firstName.value}/${lastName.value}/${standard.value}") },
        modifier = Modifier.constrainAs(submitBTN){
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(standardTF.bottom)
            bottom.linkTo(parent.bottom)
        }) {
            Text(text = "Submit")
        }
    }
}