package com.disfluency.screens.therapist.forms

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.disfluency.components.icon.IconLabeled
import com.disfluency.components.panel.FormResponsePanel
import com.disfluency.model.form.*
import com.disfluency.navigation.structure.BackNavigationScaffold
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
    val assignment = viewModel.getAssignmentById(assignmentId)

    BackNavigationScaffold(
        title = assignment.form.title,
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            FormAssignmentPanel(formAssignment = assignment)
        }
    }
}

@Composable
private fun EntrySelector(
    entries: List<FormCompletionEntry>,
    selectedIndex: MutableState<Int>
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CountIndicator(count = entries.size)

        Spacer(modifier = Modifier.width(16.dp))

        DateSelector(entries = entries, selectedIndex = selectedIndex)
    }

}

@Composable
private fun CountIndicator(count: Int){
    Card(
        modifier = Modifier.wrapContentWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.mix(color = Color.White)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        IconLabeled(
            modifier = Modifier
                .wrapContentWidth()
                .padding(8.dp),
            icon = Icons.Outlined.Refresh,
            label = count.toString(),
            iconColor = Color.White,
            labelColor = Color.White
        )
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
            .width(256.dp)
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
                    .padding(8.dp)
                    .menuAnchor()
            )
            
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .width(256.dp)
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
        modifier = modifier
            .width(240.dp),
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
                .width(184.dp)
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
        EntrySelector(entries = formAssignment.completionEntries, selectedIndex = entry)

        Spacer(modifier = Modifier.height(16.dp))

        FormResponsePanel(entry = formAssignment.completionEntries[entry.value])
    }
}
