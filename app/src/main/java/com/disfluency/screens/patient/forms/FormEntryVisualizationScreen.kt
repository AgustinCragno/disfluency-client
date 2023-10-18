package com.disfluency.screens.patient.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.panel.FormResponsePanel
import com.disfluency.navigation.structure.SinglePageExitScaffold
import com.disfluency.viewmodel.FormsViewModel

@Composable
fun FormEntryVisualizationScreen(
    assignmentId: String,
    navController: NavHostController,
    viewModel: FormsViewModel
){
    val assignment = viewModel.getAssignmentById(assignmentId)

    SinglePageExitScaffold(
        title = stringResource(R.string.your_responses),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ){
            assignment?.let {
                FormResponsePanel(entry = assignment.completionEntries.maxBy { it.date })
            }
        }
    }
}