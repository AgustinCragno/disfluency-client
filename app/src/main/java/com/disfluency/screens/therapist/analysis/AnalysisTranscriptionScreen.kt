package com.disfluency.screens.therapist.analysis

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.audio.playback.DisfluencyAudioPlayer
import com.disfluency.audio.playback.DisfluencyAudioUrlPlayer
import com.disfluency.components.audio.CompactAudioPlayer
import com.disfluency.components.scroll.verticalFadingEdge
import com.disfluency.data.mock.MockedData
import com.disfluency.model.analysis.*
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.format.formatLocalDateAsMonthInWords
import java.time.LocalDate

@Preview
@Composable
private fun AnalysisScreenPreview(){
    DisfluencyTheme() {
        AnalysisTranscriptionScreen(
            analysis = MockedData.longAnalysis,
            navController = rememberNavController()
        )
    }
}

@Composable
fun AnalysisTranscriptionScreen(
    analysis: Analysis, //TODO: temp, recibir id e ir a buscarlo al viewModel
    navController: NavHostController
){
    val url = "https://pf5302.s3.us-east-2.amazonaws.com/audios/velocidad.mp3"
    val disfluencyAudioPlayer = DisfluencyAudioUrlPlayer(LocalContext.current)

    BackNavigationScaffold(
        title = "Analisis de Disfluencias",
        navController = navController,
        actions = {
            ViewResultsAction(analysis = analysis, navController = navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TranscriptionPanel(
                analysis = analysis,
                disfluencyAudioPlayer = disfluencyAudioPlayer,
                modifier = Modifier.weight(11f)
            )

            SessionPlayerPanel(
                audioUrl = url,
                audioPlayer = disfluencyAudioPlayer,
                modifier = Modifier.weight(2f)
            )
        }

    }
}

@Composable
private fun ViewResultsAction(analysis: Analysis, navController: NavHostController){
    IconButton(onClick = { /*TODO agregar ruta y hacer el navigate*/ }) {
        Icon(imageVector = Icons.Outlined.QueryStats, contentDescription = null)
    }
}

@Composable
private fun SessionPlayerPanel(
    audioUrl: String,
    audioPlayer: DisfluencyAudioUrlPlayer,
    modifier: Modifier = Modifier
){
    LaunchedEffect(Unit){
        audioPlayer.load(audioUrl)
    }

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
            CompactAudioPlayer(audioPlayer = audioPlayer)
        }
    }
}

@Composable
private fun TranscriptionPanel(
    analysis: Analysis,
    disfluencyAudioPlayer: DisfluencyAudioPlayer,
    modifier: Modifier = Modifier
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
                    .background(Color.LightGray.copy(alpha = 0.15f))
                    .fillMaxWidth()
            ) {
                val fontSize = 18.sp
                val padding = 12.dp

                Text(
                    text = "Sesión #12",
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


            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 3.dp,
                color = Color.Gray.copy(alpha = 0.3f)
            )

            Transcription(analysis = analysis, audioPlayer = disfluencyAudioPlayer)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Transcription(analysis: Analysis, audioPlayer: DisfluencyAudioPlayer){
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
                enabled = !audioPlayer.isPlaying()
            )
    ){
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            analysis.analysedWords.forEach { word ->
                Column(
                    modifier = Modifier.wrapContentWidth()
                ) {
                    WordDisfluencyDisplay(word = word)

                    val bgColor: Color by animateColorAsState(
                        targetValue = if (word.isTimeInBetween(audioPlayer.position().toInt())) MaterialTheme.colorScheme.primary else Color.Black,
                        animationSpec = tween(50, 0, LinearEasing)
                    )

                    Text(
                        text = word.word + " ",
                        fontSize = 18.sp,
                        modifier = Modifier.clickable { audioPlayer.seekTo(word.startTime.toFloat()) },
                        color = bgColor
                    )

                    // Se arregla en androidx.compose.foundation:foundation-layout:1.5.0 que agrega al FlowRow un VerticalArrangement
//                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }

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
        if (it.size > 1)
            CompositeDisfluencyDisplay(types = it)
        else
            SingleDisfluencyDisplay(disfluency = it.single())
    }
    ?: DisfluencyDisplayText(letter = "", color = Color.Transparent)
}