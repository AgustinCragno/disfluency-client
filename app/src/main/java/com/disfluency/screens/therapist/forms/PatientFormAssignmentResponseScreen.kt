package com.disfluency.screens.therapist.forms

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.audio.fillToSize
import com.disfluency.components.icon.IconLabeled
import com.disfluency.components.panel.FormResponsePanel
import com.disfluency.components.tab.TabItem
import com.disfluency.components.tab.TabScreen
import com.disfluency.model.form.*
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.therapist.forms.burndown.FormBurnDownScreen
import com.disfluency.screens.therapist.forms.burndown.OnlyOneResponseMessageScreen
import com.disfluency.screens.therapist.forms.burndown.generateResponsesReport
import com.disfluency.utilities.color.mix
import com.disfluency.utilities.format.formatLocalDateAsWords
import com.disfluency.viewmodel.FormsViewModel
import java.time.LocalDate


@Composable
fun PatientFormAssignmentResponseScreen(
    assignmentId: String,
    navController: NavHostController,
    viewModel: FormsViewModel
){
    val assignment = viewModel.getAssignmentById(assignmentId)!!
    val report = generateResponsesReport(
        questions = assignment.form.questions,
        responses = assignment.completionEntries
    )

    val tabs = listOf(
        TabItem(
            title = stringResource(id = R.string.responses),
            iconOn = Icons.Filled.Assignment,
            iconOff = Icons.Outlined.Assignment,
            numberBadge = assignment.completionEntries.size
        ){
            FormAssignmentPanel(formAssignment = assignment)
        },
        TabItem(
            title = stringResource(id = R.string.timely),
            iconOn = Icons.Filled.Timeline,
            iconOff = Icons.Outlined.Timeline
        ){
            if (report.size > 1)
                FormBurnDownScreen(report)
            else
                OnlyOneResponseMessageScreen()
        }
    )

    BackNavigationScaffold(
        title = assignment.form.title,
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabScreen(tabs = tabs)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelector(
    entries: List<FormCompletionEntry>,
    selectedIndex: MutableState<Int>
){
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 40.dp, vertical = 16.dp)
            .width(320.dp)
            .clickable { },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer//.mix(color = Color.Red)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            DateDisplay(
                date = entries[selectedIndex.value].date,
                trailingIcon = Icons.Default.ArrowDropDown,
                modifier = Modifier
                    .width(320.dp)
                    .padding(8.dp)
                    .menuAnchor()
            )
            
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .width(280.dp)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
                entries.forEachIndexed { i, it ->
                    DropdownMenuItem(
                        modifier = Modifier.height(40.dp),
                        text = {
                            DateDisplay(
                                date = it.date,
                                color = Color.Black
                            )
                        },
                        onClick = {
                            selectedIndex.value = i
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateDisplay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    color: Color = Color.White,
    trailingIcon: ImageVector? = null
){
    val dateAsText = formatLocalDateAsWords(date, stringResource(id = R.string.locale))

    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = color
        )
        Text(
            text = dateAsText,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .height(20.dp)
                .weight(1f)
                .wrapContentHeight()
        )
        trailingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = color
            )
        }

    }
}

@Composable
private fun FormAssignmentPanel(formAssignment: FormAssignment){
    val entry = remember {
        mutableStateOf(0)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        DateSelector(entries = formAssignment.completionEntries, selectedIndex = entry)

        Spacer(modifier = Modifier.height(16.dp))

        FormResponsePanel(entry = formAssignment.completionEntries[entry.value])
    }
}
