package com.disfluency.screens.therapist.analysis

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.analysis.Analysis
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.viewmodel.AnalysisViewModel

@Preview
@Composable
private fun PatientSessionsScreenPreview(){
    val analysisViewModel = AnalysisViewModel()
    analysisViewModel.getAnalysisListByPatientId("1")

    DisfluencyTheme {
        PatientSessionsScreen(
            "1",
            navController = rememberNavController(),
            analysisViewModel
        )
    }
}

@Composable
fun PatientSessionsScreen(
    patientId: String,
    navController: NavHostController,
    viewModel: AnalysisViewModel
){
    LaunchedEffect(Unit){
        viewModel.getAnalysisListByPatientId(patientId)
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
                SessionList(list = list, navController = navController)
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
private fun SessionList(list: List<Analysis>, navController: NavHostController){
    if (list.isNotEmpty()){
        Column(Modifier.fillMaxWidth()) {
            list.forEachIndexed { index, it ->
                SessionListItem(analysis = it, index = index + 1, navController = navController)
            }
        }
    }else {
        ImageMessagePage(
            imageResource = R.drawable.record_action,
            text = stringResource(id = R.string.patient_has_no_recorded_sessions)
        )
    }
}

@Composable
private fun SessionListItem(analysis: Analysis, index: Int, navController: NavHostController){
    ListItem(
        headlineContent = {
            Text(
                text = "Sesion #$index - ${formatLocalDate(analysis.date)}",
                modifier = Modifier.clickable {
                    navController.navigate(Route.Therapist.AnalysisTranscription.routeTo(analysis.id))
                }
            )
        }
    )
}