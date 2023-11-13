package com.disfluency.components.sheet

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.button.RecordButton
import com.disfluency.components.dialogs.AnimatedDialog
import com.disfluency.components.dialogs.RecordingRecommendationsDialog
import com.disfluency.model.exercise.Exercise
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.patient.exercises.*
import com.disfluency.utilities.color.darken
import com.disfluency.viewmodel.record.RecordAudioViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

private const val BOTTOM_SHEET_PEEK_HEIGHT = 80

private const val BOTTOM_SHEET_TITLE_PADDING_OPEN = 46f
private const val BOTTOM_SHEET_TITLE_PADDING_CLOSED = 16f
private const val BOTTOM_SHEET_REQUIRED_HEIGHT = 200

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreenSheetScaffold(
    title: String,
    navController: NavHostController,
    recordViewModel: RecordAudioViewModel,
    onSend: () -> Unit,
    mainContent: @Composable () -> Unit,
    sheetContent: @Composable (State<Float>) -> Unit
){
    val context = LocalContext.current
    val isMenuExtended = remember { mutableStateOf(false) }

    val scaffoldState = rememberBottomSheetScaffoldState()

    var animateButtonScale = remember{ derivedStateOf { 1f } }
    var animateTitlePadding = remember {
        derivedStateOf { BOTTOM_SHEET_TITLE_PADDING_OPEN }
    }

    var openInfoDialog by remember {
        mutableStateOf(false)
    }

    if (openInfoDialog){
        RecordingRecommendationsDialog {
            openInfoDialog = false
        }
    }

    BackNavigationScaffold(
        title = title,
        navController = navController,
        actions = {
            IconButton(
                onClick = {
                    openInfoDialog = true
                }
            ) {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Box(
            Modifier.fillMaxSize()
        ) {
            BottomSheetScaffold(
                content = { bottomSheetPaddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(bottomSheetPaddingValues)
                    ){
                        mainContent()
                    }
                },
                sheetContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 32.dp)
                    ) {
                        sheetContent(animateTitlePadding)
                    }
                },
                sheetContainerColor = MaterialTheme.colorScheme.secondary,
                containerColor = Color.White,
                sheetPeekHeight = BOTTOM_SHEET_PEEK_HEIGHT.dp,
                sheetDragHandle = {},
                scaffoldState = scaffoldState,
                sheetSwipeEnabled = !isMenuExtended.value
            )

            RecordButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = (BOTTOM_SHEET_PEEK_HEIGHT.dp + 16.dp) / 2)
                    .scale(animateButtonScale.value),
                isMenuExtended = isMenuExtended,
                viewModel = recordViewModel,
                onPress = {
                    recordViewModel.start(context)
                },
                onRelease = {
                    recordViewModel.stop()
                },
                onDelete = {
                    recordViewModel.delete()
                },
                onPlay = {
                    recordViewModel.play()
                },
                onSend = onSend
            )
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
                BOTTOM_SHEET_TITLE_PADDING_OPEN else BOTTOM_SHEET_TITLE_PADDING_CLOSED
        )
    }
}

@Composable
fun BottomSheetTitleContent(
    animatePadding: State<Float>,
    title: String,
    content: @Composable ColumnScope.() -> Unit
){
    Column(
        modifier = Modifier
            .heightIn(BOTTOM_SHEET_REQUIRED_HEIGHT.dp)
            .padding(top = animatePadding.value.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
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

        content()
    }
}
