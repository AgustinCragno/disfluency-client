package com.disfluency.screens.therapist.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.data.mock.MockedData
import com.disfluency.model.analysis.AnalysisResults
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.therapist.analysis.results.DisfluenciesPerPhraseChart
import com.disfluency.screens.therapist.analysis.results.DisfluencyTypeCharts
import com.disfluency.screens.therapist.analysis.results.FluencyIndexChart
import com.disfluency.ui.theme.DisfluencyTheme


@Preview
@Composable
private fun ResultsScreenPreview(){
    DisfluencyTheme() {
        AnalysisResultsScreen(
            analysisResults = MockedData.analysisResults,
            navController = rememberNavController()
        )
    }
}

@Composable
fun AnalysisResultsScreen(
    analysisResults: AnalysisResults, //TODO: temp, recibir id e ir a buscarlo al viewModel
    navController: NavHostController
){
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                FluencyIndexChart(analysisResults = analysisResults)

                Spacer(modifier = Modifier.height(16.dp))

                DisfluenciesPerPhraseChart(analysisResults = analysisResults)

                Spacer(modifier = Modifier.height(16.dp))

                DisfluencyTypeCharts(analysisResults = analysisResults)
            }
        }
    }
}