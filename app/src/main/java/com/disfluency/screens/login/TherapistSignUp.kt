package com.disfluency.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.inputs.AvatarPicker
import com.disfluency.components.inputs.MandatoryTextInput
import com.disfluency.components.inputs.MandatoryValidation
import com.disfluency.components.stepper.PageStepper
import com.disfluency.components.stepper.StepScreen
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.avatar.AvatarManager
import com.disfluency.viewmodel.LoggedUserViewModel

@Composable
fun TherapistSignUpScreen(
    navController: NavHostController,
    viewModel: LoggedUserViewModel = viewModel()
){
    SignUpLobbyScaffold(title = R.string.signup, navController = navController) {

    }
}

@Composable
private fun SignUpStepper(navController: NavHostController){
    val avatarIndex = remember { mutableStateOf(0) }
    val name = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val steps = listOf(
        StepScreen(stringResource(R.string.user_info), listOf(name, lastName).all { MandatoryValidation().validate(it.value) }){
            UserInfoSelectionScreen(avatarIndex = avatarIndex, name = name, lastName = lastName)
        },
        StepScreen(stringResource(R.string.user)){

        },
        StepScreen(stringResource(R.string.confirm)){

        }
    )

    PageStepper(
        steps = steps,
        onCancel = { navController.popBackStack() },
        onFinish = {

        }
    )
}

@Composable
private fun UserInfoSelectionScreen(avatarIndex: MutableState<Int>, name: MutableState<String>, lastName: MutableState<String>){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.user_avatar),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        AvatarPicker(selectedAvatarIndex = avatarIndex)

        Text(
            modifier = Modifier.padding(top = 48.dp, bottom = 8.dp),
            text = stringResource(R.string.personal_info),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        MandatoryTextInput(state = name, label = stringResource(R.string.name))
        MandatoryTextInput(state = lastName, label = stringResource(R.string.last_name))
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview(){
    AvatarManager.initialize(LocalContext.current)

    DisfluencyTheme() {
        SignUpStepper(navController = rememberNavController())
    }
}