package com.disfluency.screens.patient

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.components.audio.AudioLiveWaveform
import com.disfluency.components.button.RecordButton
import com.disfluency.screens.login.SignUpLobbyScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.viewmodel.RecordScreenViewModel

const val BOTTOM_SHEET_PEEK_HEIGHT = 80

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RecordExerciseScreen(){
    val isMenuExtended = remember { mutableStateOf(false) }

    val scaffoldState = rememberBottomSheetScaffoldState()

    var animateButtonScale: State<Float> = derivedStateOf { 1f }
    var animateTitlePadding: State<Float> = derivedStateOf { 46f }

    DisfluencyTheme() {
        SignUpLobbyScaffold(title = R.string.practice, navController = rememberNavController()) { paddingValues ->
            Box(
                Modifier.fillMaxSize()
            ) {
                BottomSheetScaffold(
                    sheetContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .padding(horizontal = 32.dp)
                        ){
                            ExerciseInstructionsPanel(animatePadding = animateTitlePadding)
                        }
                    },
                    sheetContainerColor = MaterialTheme.colorScheme.secondary,
                    containerColor = Color.White,
                    sheetPeekHeight = BOTTOM_SHEET_PEEK_HEIGHT.dp,
                    sheetDragHandle = {},
                    scaffoldState = scaffoldState,
                    sheetSwipeEnabled = !isMenuExtended.value
                ) { bottomSheetPaddingValues ->

                    ExercisePhrasePanel(
                        modifier = Modifier
                            .padding(bottomSheetPaddingValues)
                            .padding(paddingValues)
                    )
                }
                RecordButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = (BOTTOM_SHEET_PEEK_HEIGHT.dp + 16.dp) / 2)
                        .scale(animateButtonScale.value),
                    isMenuExtended = isMenuExtended
                )
            }


        }
    }

    val initialMeasure: MutableState<Float?> = remember { mutableStateOf(null) }
    LaunchedEffect(Unit){
        initialMeasure.value = scaffoldState.bottomSheetState.requireOffset() - 1f
    }

    initialMeasure.value?.let {
        animateButtonScale = animateFloatAsState(
            targetValue = if (scaffoldState.bottomSheetState.requireOffset() >= it)
                1f else 0f,
            animationSpec = tween(100)
        )

        animateTitlePadding = animateFloatAsState(
            targetValue = if (scaffoldState.bottomSheetState.requireOffset() >= it * 0.7f)
                46f else 16f
        )
    }
}

@Composable
private fun ExerciseInstructionsPanel(animatePadding: State<Float>){
    Column(
        modifier = Modifier
            .padding(top = animatePadding.value.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Fonacion Continuada",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            thickness = 3.dp,
            color = Color.Black.copy(alpha = 0.3f)
        )

        Text(
            text = "Mantener la fonacion a lo largo de la palabra y entre palabra y palabra. Mantener la vibracion de las cuerdas vocales a lo largo de la palabra y entre palabras sin frenar, y sostener durante toda una frase",
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        AudioPlayerPanel()
    }
}

@Composable
private fun AudioPlayerPanel(viewModel: RecordScreenViewModel = viewModel()){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AudioLiveWaveform(amplitudes = viewModel.audioAmplitudes, maxHeight = 96.dp)
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "00:00",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )

            Text(
                text = "00:28",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        IconButton(
            onClick = {  },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ExercisePhrasePanel(modifier: Modifier){
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "La portabilidad es la capacidad del producto o componente de ser transferido de forma efectiva y eficiente de un entorno hardware, software, operacional o de utilización a otro",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
    }
}