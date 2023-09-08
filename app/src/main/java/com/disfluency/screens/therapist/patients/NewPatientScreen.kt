package com.disfluency.screens.therapist.patients


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.IconLabeled
import com.disfluency.components.inputs.*
import com.disfluency.components.inputs.avatar.AvatarPicker
import com.disfluency.components.inputs.date.DateInput
import com.disfluency.components.inputs.date.WeeklyTimePicker
import com.disfluency.components.inputs.date.WeeklyTurnPicker
import com.disfluency.components.inputs.text.*
import com.disfluency.components.stepper.PageStepper
import com.disfluency.components.stepper.StepScreen
import com.disfluency.model.user.Patient
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.utilities.avatar.AvatarManager
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.utilities.format.formatWeeklyTurn
import com.disfluency.viewmodel.PatientsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun NewPatientScreen(
    therapist: Therapist,
    navController: NavHostController,
    viewModel: PatientsViewModel = viewModel()
){
    BackNavigationScaffold(
        title = stringResource(R.string.new_patient),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NewPatientForms(therapist = therapist, navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
private fun NewPatientForms(
    therapist: Therapist,
    navController: NavController,
    viewModel: PatientsViewModel = viewModel()
){
    val avatarIndex = remember { mutableStateOf(0) }
    val name = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val dni = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val dateOfBirth: MutableState<LocalDate?> = remember { mutableStateOf(null) }
    val weeklyTurn = remember{ mutableStateListOf<DayOfWeek>() }
    val weeklyHour: MutableState<LocalTime?> = remember { mutableStateOf(null) }


    var submitted by remember { mutableStateOf(false) }

    val steps = listOf(
        StepScreen("Avatar"){
            AvatarSelectionScreen(avatarIndex = avatarIndex)
        },
        StepScreen("Datos", validateDataInputs(name, lastName, dni, email, dateOfBirth)){
            PatientDataScreen(name = name, lastName = lastName, id = dni, email = email, dateOfBirth = dateOfBirth)
        },
        StepScreen("Turno", validateTurnInputs(weeklyTurn, weeklyHour)){
            TurnSelectionScreen(weeklyTurn = weeklyTurn, weeklyHour = weeklyHour)
        },
        StepScreen("Confirmar"){
            ConfirmationScreen(
                avatar = AvatarManager.getAvatarId(avatarIndex.value),
                name = name.value,
                lastName = lastName.value,
                id = dni.value,
                email = email.value,
                dateOfBirth = dateOfBirth.value!!,
                weeklyTurn = weeklyTurn,
                weeklyHour = weeklyHour.value!!
            )
        }
    )

    PageStepper(
        steps = steps,
        onCancel = {
            navController.popBackStack()
        },
        onFinish = {
            val patient = Patient(
                name = name.value,
                lastName = lastName.value,
                id = dni.value,
                dateOfBirth = dateOfBirth.value!!,
                email = email.value,
                joinedSince = LocalDate.now(),
                weeklyHour = weeklyHour.value!!,
                weeklyTurn = weeklyTurn,
                avatarIndex = avatarIndex.value
            )
            viewModel.createPatientForTherapist(therapist.id, patient)
            CoroutineScope(Dispatchers.IO).launch {
                delay(200)
                submitted = true
            }
        }
    )

    if (submitted){
        LaunchedEffect(Unit){
            navController.popBackStack()
            navController.navigate(Route.Therapist.ConfirmationNewPatient.path)
        }
    }
}

@Composable
private fun AvatarSelectionScreen(avatarIndex: MutableState<Int>){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AvatarPicker(selectedAvatarIndex = avatarIndex)
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Seleccione un avatar de usuario",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PatientDataScreen(name: MutableState<String>, lastName: MutableState<String>, id: MutableState<String>, email: MutableState<String>, dateOfBirth: MutableState<LocalDate?>){
    Column {
        MandatoryTextInput(state = name, label = stringResource(R.string.name))
        MandatoryTextInput(state = lastName, label = stringResource(R.string.last_name))
        MandatoryDigitsInput(state = id, label = stringResource(R.string.person_id))
        MandatoryEmailInput(state = email, label = stringResource(R.string.email))
        DateInput(state = dateOfBirth, label = stringResource(R.string.date_of_birth))
    }
}

private fun validateDataInputs(name: MutableState<String>, lastName: MutableState<String>, id: MutableState<String>, email: MutableState<String>, dateOfBirth: MutableState<LocalDate?>): Boolean {
    return listOf(name, lastName, id, email).all { MandatoryValidation().validate(it.value) }
            && DigitsOnlyValidation().validate(id.value)
            && EmailValidation().validate(email.value)
            && dateOfBirth.value != null
}

@Composable
private fun TurnSelectionScreen(weeklyTurn: SnapshotStateList<DayOfWeek>, weeklyHour: MutableState<LocalTime?>){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.weekly_turn), color = MaterialTheme.colorScheme.primary)
        WeeklyTurnPicker(selectedDays = weeklyTurn)
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = stringResource(R.string.weekly_hour), color = MaterialTheme.colorScheme.primary)
        WeeklyTimePicker(state = weeklyHour, stringResource(R.string.weekly_hour))
    }
}

private fun validateTurnInputs(weeklyTurn: SnapshotStateList<DayOfWeek>, weeklyHour: MutableState<LocalTime?>): Boolean{
    return weeklyTurn.isNotEmpty() && weeklyHour.value != null
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConfirmationScreen(avatar: Int, name: String, lastName: String, id: String, email: String, dateOfBirth: LocalDate, weeklyTurn: List<DayOfWeek>, weeklyHour: LocalTime){

    Column(modifier = Modifier.width(280.dp)) {
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = avatar),
                    contentDescription = "User Thumbnail",
                    modifier = Modifier
                        .size(90.dp)
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "$name $lastName",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = id,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val age = Period.between(dateOfBirth, LocalDate.now()).years
                IconLabeled(
                    icon = Icons.Outlined.AutoAwesome,
                    label = "$age años"
                )

                Spacer(modifier = Modifier.width(16.dp))

                val date = formatLocalDate(dateOfBirth)
                IconLabeled(
                    icon = Icons.Outlined.Cake,
                    label = "nacido un $date"
                )
            }
        }

        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                IconLabeled(
                    icon = Icons.Outlined.ForwardToInbox,
                    label = email
                )
            }
        }

        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconLabeled(
                    icon = Icons.Outlined.CalendarMonth,
                    label = formatWeeklyTurn(weeklyTurn)
                )

                Spacer(modifier = Modifier.width(16.dp))

                IconLabeled(
                    icon = Icons.Outlined.AccessTime,
                    label = weeklyHour.format(DateTimeFormatter.ofPattern(stringResource(R.string.time_format)))
                )
            }
        }


    }
}
