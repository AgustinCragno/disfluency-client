package com.disfluency.screens.therapist.analysis

import androidx.compose.animation.*
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.audio.AudioMediaType
import com.disfluency.components.audio.CompactAudioPlayer
import com.disfluency.components.scroll.verticalFadingEdge
import com.disfluency.data.mock.MockedData
import com.disfluency.model.analysis.AnalysedWord
import com.disfluency.model.analysis.Analysis
import com.disfluency.model.analysis.DisfluencyCategory.QUALITATIVE
import com.disfluency.model.analysis.DisfluencyCategory.QUANTITATIVE
import com.disfluency.model.analysis.DisfluencyType
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.color.lighten
import com.disfluency.utilities.format.formatLocalDateAsMonthInWords
import com.disfluency.viewmodel.AnalysisViewModel
import com.disfluency.viewmodel.ExercisesViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

@Preview
@Composable
private fun AnalysisScreenPreview(){
    val analysisViewModel = AnalysisViewModel()
    analysisViewModel.patientAnalysis.value = listOf(MockedData.longAnalysis)

    DisfluencyTheme() {
        SessionTranscriptionScreen(
            "1",
            navController = rememberNavController(),
            analysisViewModel
        )
    }
}

@Composable
fun ExerciseTranscriptionScreen(
    practiceId: String,
    navController: NavHostController,
    viewModel: ExercisesViewModel,
    analysisViewModel: AnalysisViewModel
) {
    LaunchedEffect(Unit){
        viewModel.getAnalysisByExercisePracticeId(practiceId)
    }
    var title = "Ejercicio"
    viewModel.analysis.value?.let {
        AnalysisTranscription(analysis = it, title = title, navController = navController, viewModel = analysisViewModel)
    }
        ?:
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
}

@Composable
fun SessionTranscriptionScreen(
    analysisId: String,
    navController: NavHostController,
    viewModel: AnalysisViewModel
) {
    val analysis = viewModel.getAnalysis(analysisId)
    val index = viewModel.getSessionIndex(analysis)
    var title = "Sesión #$index"

//    analysis.saveOriginalAnalysis()

    AnalysisTranscription(analysis = analysis, title = title, navController = navController, viewModel = viewModel)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnalysisTranscription(
    analysis: Analysis,
    title: String,
    navController: NavHostController,
    viewModel: AnalysisViewModel
){
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    val isInEditMode = remember {
        mutableStateOf(false)
    }

    var isInWordEditMode by remember { mutableStateOf(false) }
    val wordForEdit = remember {
        mutableStateOf<AnalysedWord?>(null)
    }

    LaunchedEffect(!isInWordEditMode){
        if (wordForEdit.value != null){
            focusRequester.freeFocus()
            keyboard?.hide()
        }
    }

    BackNavigationScaffold(
        title =
            if (isInEditMode.value)
                stringResource(R.string.manual_editor)
            else
                stringResource(R.string.disfluency_analisis),
        navController = navController,
        actions = {
            if (isInEditMode.value)
                SubmitEditAction(analysis = analysis, editing = isInEditMode, viewModel = viewModel)
            else
                ViewResultsAction(analysis = analysis, navController = navController)
        },
        background =
            if (isInEditMode.value)
                MaterialTheme.colorScheme.onPrimaryContainer.lighten(0.2f)
            else
                Color.Transparent,
//        onBackNavigation = {
//            if (isInEditMode.value)
//                analysis.resetToOriginalAnalysis()
//            else
//                navController.popBackStack()
//            //TODO: cancelar el edit si va para atras cuando esta editando
//        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TranscriptionPanel(
                    analysis = analysis,
                    title = title,
                    modifier = Modifier.weight(11f),
                    onWordEdit = {
                        wordForEdit.value = it
                        isInWordEditMode = true
                    },
                    editing = isInEditMode
                )

                SessionPlayerPanel(
                    audioUrl = analysis.audioUrl,
                    modifier = Modifier.weight(1.5f)
                )
            }

            val animationTime = 500

            AnimatedVisibility(
                visible = isInWordEditMode,
                enter = fadeIn(tween(animationTime)),
                exit = fadeOut(tween(animationTime))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f))
                        .clickable(false) { }
                )
            }

            AnimatedVisibility(
                visible = isInWordEditMode,
                enter = slideInVertically(tween(animationTime)) { it + 200 },
                exit = slideOutVertically(tween(animationTime, animationTime / 2)) { it + 200 }
            ) {
                Box(modifier = Modifier.fillMaxSize()){
                    WordEditTexField(
                        word = wordForEdit.value!!.word,
                        focusRequester = focusRequester,
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onSubmit = {
                            isInWordEditMode = false

                            if (it != wordForEdit.value!!.word){
                                isInEditMode.value = true
                                wordForEdit.value!!.word = it
                            }
                        }
                    )

                    LaunchedEffect(Unit){
                        delay((animationTime * 0.3).toLong())
                        focusRequester.requestFocus()
                        delay(100)
                        keyboard?.show()
                    }
                }
            }
        }
    }
}

@Composable
private fun ViewResultsAction(analysis: Analysis, navController: NavHostController){
    IconButton(
        onClick = {
            navController.navigate(Route.Therapist.AnalysisResults.routeTo(analysis.id))
        },
        enabled = analysis != null
    ) {
        Icon(imageVector = Icons.Outlined.QueryStats, contentDescription = null)
    }
}

@Composable
private fun SubmitEditAction(analysis: Analysis, editing: MutableState<Boolean>, viewModel: AnalysisViewModel){
    IconButton(
        onClick = {
            editing.value = false
            viewModel.updateAnalysis(analysis)
        }
    ) {
        Icon(imageVector = Icons.Filled.Done, contentDescription = null)
    }
}

@Composable
private fun SessionPlayerPanel(
    audioUrl: String,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 0.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ){
                CompactAudioPlayer(url = audioUrl, type = AudioMediaType.URL)
            }
        }
    }
}

@Composable
private fun TranscriptionPanel(
    analysis: Analysis,
    title: String,
    modifier: Modifier = Modifier,
    editing: MutableState<Boolean>,
    onWordEdit: (AnalysedWord) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalFadingEdge(scrollState, length = 100.dp, edgeColor = Color.White)
            .verticalScroll(state = scrollState)
    ) {
        SessionHeader(title = title)

        Transcription(
            analysis = analysis,
            updateEditing = { editing.value = it },
            onWordEdit = onWordEdit
        )
    }
}

@Composable
private fun SessionHeader(
    title: String
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Color.LightGray.copy(
                        alpha = 0.15f
                    )
                )
        ) {
            val fontSize = 18.sp
            val padding = 12.dp

            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
                modifier = Modifier.padding(top = padding, bottom = padding, start = padding, end = padding)
            )

            Text(
                text = formatLocalDateAsMonthInWords(LocalDate.now(), stringResource(id = R.string.locale)),
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSize,
                modifier = Modifier.padding(top = padding, bottom = padding, end = padding)
            )
        }
    }

}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
private fun Transcription(
    analysis: Analysis,
    updateEditing: (Boolean) -> Unit = {},
    onWordEdit: (AnalysedWord) -> Unit = {}
){

    Box(
        modifier = Modifier
    ){
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            analysis.analysedWords?.forEach { word ->
                Column(
                    modifier = Modifier.wrapContentWidth()
                ) {
                    WordDisfluencyDisplay(word = word)

                    var expanded by remember { mutableStateOf(false) }
                    var nestedExpanded by remember { mutableStateOf(false) }

                    Box {
                        Text(
                            text = word.word + " ",
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {  },
                                    onLongClick = { expanded = true },
                                )
                        )
                        DisfluencySelectionMainMenu(
                            expanded = expanded,
                            word = word,
                            updateEditing = updateEditing,
                            updateNestedExpanded = { nestedExpanded = it },
                            updateExpanded = { expanded = it },
                            onWordEdit = onWordEdit
                        )
                        DisfluencySelectionNestedMenu(nestedExpanded, word, updateEditing) { nestedExpanded = it }
                    }
                }
            }
        }
    }
}

@Composable
private fun DisfluencySelectionNestedMenu(
    expanded: Boolean,
    word: AnalysedWord,
    updateEditing: (Boolean) -> Unit,
    updateExpanded: (Boolean) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { updateExpanded(false) },
    ) {
        DisfluencyType.values().filter { d -> d.category == QUALITATIVE }.forEach {
            DropdownMenuItem(
                text = { Text(it.fullName) },
                onClick = {
                    handleDisfluencyClick(word, it)
                    updateExpanded(false)
                    updateEditing(true)
                },
                leadingIcon = {
                    Text(
                        text = it.name,
                        color = it.color,
                    )
                },
                modifier = Modifier.background(
                    if (word.hasDisfluency(it)) Color.LightGray else Color.Unspecified
                )
            )
        }
    }
}

@Composable
private fun DisfluencySelectionMainMenu(
    expanded: Boolean,
    word: AnalysedWord,
    updateEditing: (Boolean) -> Unit,
    updateNestedExpanded: (Boolean) -> Unit,
    updateExpanded: (Boolean) -> Unit,
    onWordEdit: (AnalysedWord) -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = { updateExpanded(false) }) {
        DisfluencyType.values().filter { d -> d.category == QUANTITATIVE }.forEach {
            DropdownMenuItem(
                text = { Text(it.fullName) },
                onClick = {
                    handleDisfluencyClick(word, it)
                    updateExpanded(false)
                    updateEditing(true)
                },
                leadingIcon = {
                    Text(
                        text = it.name,
                        color = it.color,
                    )
                },
                modifier = Modifier.background(
                    if (word.hasDisfluency(it)) Color.LightGray else Color.Unspecified
                )
            )
        }
        Divider()
        DropdownMenuItem(
            text = { Text("Cualitativas") },
            onClick = {
                updateNestedExpanded(true)
                updateExpanded(false)
                      },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = null
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Outlined.ArrowRight,
                    contentDescription = null
                )
            }
        )
        DropdownMenuItem(
            text = { Text("Editar palabra") },
            onClick = {
                onWordEdit.invoke(word)
                updateExpanded(false)
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = null
                )
            })
    }
}

private fun handleDisfluencyClick(
    word: AnalysedWord,
    it: DisfluencyType
) {
    if (word.hasDisfluency(it)) word.removeDisfluency(it) else word.addDisfluency(it)

}

@Composable
private fun DisfluencyDisplayText(letter: String, color: Color){
    Text(
//            modifier = Modifier.padding(horizontal = 6.dp, vertical = 0.dp),
        text = letter,
        style = MaterialTheme.typography.displayMedium,
//            fontWeight = FontWeight.ExtraBold,
        fontSize = 13.sp,
        color = color
    )
}

@Composable
private fun DisfluencyBracketsText(letter: String){
    Text(
        text = letter,
        fontSize = 13.sp,
        fontWeight = FontWeight.ExtraLight,
        color = Color.Black.copy(alpha = 0.8f)
    )
}

@Composable
private fun SingleDisfluencyDisplay(disfluency: DisfluencyType){
    DisfluencyDisplayText(
        letter = disfluency.name,
        color = disfluency.color
    )
}

@Composable
private fun CompositeDisfluencyDisplay(types: List<DisfluencyType>){
    Row() {
        DisfluencyBracketsText(letter = "[")

        types.forEachIndexed { index, disfluency ->
            if (index > 0)
                DisfluencyBracketsText(letter = "+")

            SingleDisfluencyDisplay(disfluency)
        }

        DisfluencyBracketsText(letter = "]")
    }
}

@Composable
private fun WordDisfluencyDisplay(word: AnalysedWord){
    word.disfluency?.let {
        if (it.isNotEmpty()){ //TODO: ver como hacer esto bien
            if (it.size > 1)
                CompositeDisfluencyDisplay(types = it)
            else
                SingleDisfluencyDisplay(disfluency = it.single())
        }else{
            DisfluencyDisplayText(letter = "", color = Color.Transparent)
        }

    }
    ?: DisfluencyDisplayText(letter = "", color = Color.Transparent)
}

@Composable
private fun WordEditTexField(
    modifier: Modifier = Modifier,
    word: String,
    focusRequester: FocusRequester,
    onSubmit: (String) -> Unit
){
    var state by remember {
        mutableStateOf(
            TextFieldValue(
                text = word,
                selection = TextRange(word.length)
            )
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 0.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 8.dp,
            topEnd = 8.dp
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state,
                onValueChange = { state = it },
                modifier = Modifier
                    .fillMaxHeight()
                    .focusRequester(focusRequester)
                    .weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { onSubmit.invoke(state.text) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = modifier
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}