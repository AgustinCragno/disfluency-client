package com.disfluency.screens.therapist.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.data.mock.MockedData
import com.disfluency.model.analysis.Analysis
import com.disfluency.model.analysis.AnalysisResults
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.therapist.analysis.results.DisfluenciesPerPhraseChart
import com.disfluency.screens.therapist.analysis.results.DisfluencyTypeCharts
import com.disfluency.screens.therapist.analysis.results.FluencyIndexChart
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.viewmodel.AnalysisViewModel


@Preview
@Composable
private fun ResultsScreenPreview(){
    val analysisViewModel = AnalysisViewModel()
    analysisViewModel.getAnalysisListByPatientId("1234")

    DisfluencyTheme() {
        AnalysisResultsScreen(
            "1",
            navController = rememberNavController(),
            analysisViewModel
        )
    }
}

@Composable
fun AnalysisResultsScreen(
    analysisId: String,
    navController: NavHostController,
    viewModel: AnalysisViewModel
){
    val analysisResults = viewModel.getAnalysis(analysisId).results

    val scrollState = rememberScrollState()

    BackNavigationScaffold(
        title = "Analisis de Disfluencias",
        navController = navController
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            analysisResults?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    FluencyIndexChart(analysisResults = it)

                    Spacer(modifier = Modifier.height(16.dp))

                    DisfluenciesPerPhraseChart(analysisResults = it)

                    Spacer(modifier = Modifier.height(16.dp))

                    DisfluencyTypeCharts(analysisResults = it)
                }
            }
        }
    }
}