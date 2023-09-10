package com.disfluency.screens.therapist.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.ListItem
import com.disfluency.data.mock.MockedData
import com.disfluency.model.analysis.Analysis
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.viewmodel.AnalysisViewModel
import com.disfluency.viewmodel.RecordSessionViewModel
import com.disfluency.viewmodel.states.ConfirmationState
import java.time.LocalDate

@Preview
@Composable
private fun PatientSessionsScreenPreview(){
    val analysisViewModel = AnalysisViewModel()
    val recordViewModel = RecordSessionViewModel(LocalContext.current, LocalLifecycleOwner.current)
    analysisViewModel.patientAnalysis.value = listOf(MockedData.analysis, MockedData.longAnalysis)

    DisfluencyTheme {
        PatientSessionsScreen(
            "1",
            navController = rememberNavController(),
            analysisViewModel,
            recordViewModel
        )
    }
}

@Composable
fun PatientSessionsScreen(
    patientId: String,
    navController: NavHostController,
    viewModel: AnalysisViewModel,
    recordSessionViewModel: RecordSessionViewModel,
){
    LaunchedEffect(Unit){
        viewModel.getAnalysisListByPatientId(patientId)
    }

    if (
        recordSessionViewModel.uploadConfirmationState.value == ConfirmationState.SUCCESS
        || recordSessionViewModel.uploadConfirmationState.value == ConfirmationState.ERROR
    ){
        LaunchedEffect(Unit){
            viewModel.getAnalysisListByPatientId(patientId, onComplete = {
                recordSessionViewModel.uploadConfirmationState.value = ConfirmationState.DONE
            })
        }
    }

//    DisposableEffect(Lifecycle.Event.ON_STOP){
//        onDispose {
//            viewModel.patientAnalysis.value = null
//        }
//    }

//    BackHandler() {
//        viewModel.patientAnalysis.value = null
//    }

    BackNavigationScaffold(
        title = "Sesiones",
        navController = navController
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            viewModel.patientAnalysis.value?.let { list ->
                SessionList(list = list, navController = navController, recordViewModel = recordSessionViewModel)
            }
                ?:
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }

            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.Therapist.NewSession.routeTo(patientId))
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(imageVector = Icons.Default.Mic, contentDescription = null)
            }
        }
    }
}

@Composable
private fun SessionList(list: List<Analysis>, navController: NavHostController, recordViewModel: RecordSessionViewModel){
    Column(
        Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        if (list.isNotEmpty() || recordViewModel.uploadConfirmationState.value != ConfirmationState.DONE){
            LazyColumn(
                contentPadding = PaddingValues(top = 16.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                itemsIndexed(list){ index, it ->
                    SessionListItem(analysis = it, index = index + 1, navController = navController)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                ImageMessagePage(
                    imageResource = R.drawable.record_action,
                    text = stringResource(id = R.string.patient_has_no_recorded_sessions)
                )
            }
        }

        if (recordViewModel.uploadConfirmationState.value != ConfirmationState.DONE){
            PendingSessionListItem(index = list.size + 1)
        }
    }
}

@Composable
private fun SessionListItem(analysis: Analysis, index: Int, navController: NavHostController){
    ListItem(
        title = "Sesion #$index",
        leadingContent = {
            Icon(
                imageVector = Icons.Default.HeadsetMic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            Text(
                text = formatLocalDate(analysis.date),
                fontSize = 13.sp
            )
        },
        onClick = {
            navController.navigate(
                Route.Therapist.AnalysisTranscription.routeTo(analysis.id)
            )
        }
    )
}

@Composable
private fun PendingSessionListItem(index: Int){
    ListItem(
        title = "Sesion #$index",
        subtitle = "en proceso de analisis",
        subtitleColor = MaterialTheme.colorScheme.secondary,
        leadingContent = {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        },
        trailingContent = {
            Text(
                text = formatLocalDate(LocalDate.now()),
                fontSize = 13.sp
            )
        }
    )
}