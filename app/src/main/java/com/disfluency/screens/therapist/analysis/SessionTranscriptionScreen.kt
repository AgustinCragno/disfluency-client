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
        title = stringResource(R.string.disfluency_analisis),
        navController = navController,
        actions = {
            ViewResultsAction(analysis = analysis, navController = navController)
        }
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
                    viewModel = viewModel,
//                disfluencyAudioPlayer = disfluencyAudioPlayer,
                    modifier = Modifier.weight(11f),
                    onWordEdit = {
                        wordForEdit.value = it
                        isInWordEditMode = true
                    },
                    editing = isInEditMode
                )

                SessionPlayerPanel(
                    audioUrl = analysis.audioUrl,
//                audioPlayer = disfluencyAudioPlayer,
                    modifier = Modifier.weight(2f)
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
private fun SessionPlayerPanel(
    audioUrl: String,
//    audioPlayer: DisfluencyAudioUrlPlayer,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 0.dp),
        colors = CardDefaults.cardColors(Color.White),
//        shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ){
//            AudioPlayer(url = audioUrl, AudioMediaType.URL)
            CompactAudioPlayer(url = audioUrl, type = AudioMediaType.URL)
        }
    }
}

@Composable
private fun TranscriptionPanel(
    analysis: Analysis,
    title: String,
    viewModel: AnalysisViewModel,
//    disfluencyAudioPlayer: DisfluencyAudioPlayer,
    modifier: Modifier = Modifier,
    editing: MutableState<Boolean>,
    onWordEdit: (AnalysedWord) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .background(
                        if (editing.value) MaterialTheme.colorScheme.surface else Color.LightGray.copy(
                            alpha = 0.15f
                        )
                    )
                    .fillMaxWidth()
            ) {
                val fontSize = 18.sp
                val padding = 12.dp

                if (editing.value) {
                    Box(Modifier.fillMaxWidth()) {
                        IconButton(
                            onClick = {
                                editing.value = false
                                viewModel.updateAnalysis(analysis)
                            },
                            modifier = Modifier.align(Alignment.CenterStart)) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Back"
                            )
                        }

                        Text(
                            text = "Editor manual",
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = fontSize,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(vertical = padding)
                        )
                    }
                } else {
                    Text(
                        text = title,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize,
                        modifier = Modifier.padding(padding)
                    )

                    Text(
                        text = formatLocalDateAsMonthInWords(LocalDate.now(), stringResource(id = R.string.locale)),
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = fontSize,
                        modifier = Modifier.padding(vertical = padding)
                    )
                }
            }

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 3.dp,
                color = Color.Gray.copy(alpha = 0.3f)
            )

            Transcription(
                analysis = analysis,
                updateEditing = { editing.value = it },
                onWordEdit = onWordEdit
//                audioPlayer = disfluencyAudioPlayer
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
private fun Transcription(
    analysis: Analysis,
    updateEditing: (Boolean) -> Unit = {},
    onWordEdit: (AnalysedWord) -> Unit = {},
//    audioPlayer: DisfluencyAudioPlayer
){
    //TODO: auto-scroll on play
    val scrollState = rememberScrollState()

    //TODO: las palabras cortitas no se llegan a pintar en naranja cuando las reproduce
    // porque el tiempo entre los timestamps es muy chico, habria que ver alguna forma de
    // evitar eso

    Box(
        modifier = Modifier
            .verticalFadingEdge(scrollState, length = 100.dp, edgeColor = Color.White)
            .verticalScroll(
                state = scrollState,
//                enabled = !audioPlayer.isPlaying()
            )
    ){
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            analysis.analysedWords?.forEach { word ->
                Column(
                    modifier = Modifier.wrapContentWidth()
                ) {
                    WordDisfluencyDisplay(word = word)

//                    val bgColor: Color by animateColorAsState(
//                        targetValue = if (word.isTimeInBetween(audioPlayer.position().toInt())) MaterialTheme.colorScheme.primary else Color.Black,
//                        animationSpec = tween(50, 0, LinearEasing)
//                    )
                    var expanded by remember { mutableStateOf(false) }
                    var nestedExpanded by remember { mutableStateOf(false) }

                    Box {
                        Text(
                            text = word.word + " ",
                            fontSize = 18.sp,
//                        modifier = Modifier.clickable { audioPlayer.seekTo(word.startTime.toFloat()) },
                            color = /*bgColor*/ Color.Black,
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


                    // Se arregla en androidx.compose.foundation:foundation-layout:1.5.0 que agrega al FlowRow un VerticalArrangement
//                    Spacer(modifier = Modifier.height(2.dp))
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