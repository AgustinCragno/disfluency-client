package com.disfluency.components.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.disfluency.ui.theme.DisfluencyTheme

/**
 * Design inspired from https://github.com/jurajkusnier/fluid-bottom-navigation
 */

@Preview
@Composable
private fun FloatingButtonGroupPreview2(){
    val isMenuExtended = remember { mutableStateOf(false) }

    DisfluencyTheme() {
        RecordButton(modifier = Modifier, isMenuExtended = isMenuExtended)
    }
}

@Composable
fun RecordButton(modifier: Modifier, isMenuExtended: MutableState<Boolean>){
    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 550,
            easing = LinearEasing
        )
    )

    FabGroup(
        modifier = modifier,
        animationProgress = fabAnimationProgress,
        isMenuExtended = isMenuExtended
    )
}

@Composable
private fun FabGroup(
    modifier: Modifier,
    animationProgress: Float = 0f,
    isMenuExtended: MutableState<Boolean>
) {
    Box(
        modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {

        AnimatedFab(
            icon = Icons.Outlined.Delete,
            modifier = Modifier
                .padding(
                    PaddingValues(
                        bottom = 72.dp,
                        end = 210.dp
                    ) * FastOutSlowInEasing.transform(0f, 0.8f, animationProgress)
                ),
            opacity = LinearEasing.transform(0.2f, 0.7f, animationProgress),
            backgroundColor = MaterialTheme.colorScheme.primary,
            onClick = {
                print("Delete Action")
                isMenuExtended.value = false
            }
        )

        AnimatedFab(
            icon = Icons.Default.PlayArrow,
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 88.dp,
                ) * FastOutSlowInEasing.transform(0.1f, 0.9f, animationProgress)
            ),
            opacity = LinearEasing.transform(0.3f, 0.8f, animationProgress),
            backgroundColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = {
                print("Play Action")
            }
        )

        AnimatedFab(
            icon = Icons.Outlined.Send,
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 72.dp,
                    start = 210.dp
                ) * FastOutSlowInEasing.transform(0.2f, 1.0f, animationProgress)
            ),
            opacity = LinearEasing.transform(0.4f, 0.9f, animationProgress),
            backgroundColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = {
                print("Send Action")
                isMenuExtended.value = false
            }
        )

        MicButton(
            animationProgress = animationProgress,
            isMenuExtended = isMenuExtended,
            onPress = { print("Record Action") },
            onRelease = { print("Finished Recording") }
        )
    }
}

@Composable
private fun AnimatedFab(
    modifier: Modifier,
    icon: ImageVector? = null,
    opacity: Float = 1f,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        onClick = onClick,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        containerColor = backgroundColor,
        shape = CircleShape,
        modifier = modifier.scale(1.05f)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White.copy(alpha = opacity)
            )
        }
    }
}

@Composable
private fun MicButton(
    animationProgress: Float = 0f,
    isMenuExtended: MutableState<Boolean>,
    onPress: () -> Unit,
    onRelease: () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var startedRecording by remember { mutableStateOf(false) }

    val hasRecorded = (!isPressed && startedRecording)

    val animatedButtonColor = animateColorAsState(
        targetValue = if (isPressed) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.secondary
    )

    val animatedButtonColor2 = animateColorAsState(
        targetValue = if (animationProgress > 0.5f) MaterialTheme.colorScheme.surfaceTint
            else animatedButtonColor.value
    )

    val animateSize = animateFloatAsState(
        targetValue = if (isPressed) 1.5f else 1.05f
    )

    IconButton(
        onClick = {  },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = animatedButtonColor2.value,
            disabledContainerColor = animatedButtonColor2.value
        ),
        modifier = Modifier
            .size(56.dp)
            .scale(animateSize.value)
            .border(3.dp, Color.White, CircleShape)
            .rotate(
                225 * FastOutSlowInEasing
                    .transform(0.35f, 0.65f, animationProgress)
            ),
        interactionSource = interactionSource,
        enabled = !isMenuExtended.value
    ) {
        Icon(
            modifier = Modifier
                .scale(1.05f / animateSize.value),
            imageVector = if (animationProgress < 0.5f) Icons.Filled.Mic else Icons.Filled.Add,
            contentDescription = null,
            tint = Color.White.copy(alpha = 1f - animationProgress * (if (animationProgress > 0.5f) 0 else 1))
        )


        if (isPressed) {

            if (!startedRecording){
                onPress()
                startedRecording = true
            }

            //Use if + DisposableEffect to wait for the press action is completed
            DisposableEffect(Unit) {
                onDispose {
                    onRelease()
                    isMenuExtended.value = true
                }
            }
        }
    }
}


private fun Easing.transform(from: Float, to: Float, value: Float): Float {
    return transform(((value - from) * (1f / (to - from))).coerceIn(0f, 1f))
}

private operator fun PaddingValues.times(value: Float): PaddingValues = PaddingValues(
    top = calculateTopPadding() * value,
    bottom = calculateBottomPadding() * value,
    start = calculateStartPadding(LayoutDirection.Ltr) * value,
    end = calculateEndPadding(LayoutDirection.Ltr) * value
)