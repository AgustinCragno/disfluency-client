package com.disfluency.components.thumbnail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.model.exercise.Exercise
import com.disfluency.utilities.color.stringToRGB

@Composable
fun ExerciseThumbnail(exercise: Exercise){
    val color = stringToRGB(exercise.title)
    Surface(
        color = color,
        modifier = Modifier
            .clip(CircleShape)
            .size(40.dp)
            .border(
                1.5.dp,
                color
                    .copy(0.5f)
                    .compositeOver(Color.Black),
                CircleShape
            ),
    ) {
        Box(contentAlignment = Alignment.Center){
            Text(
                text = exercise.title.first().uppercaseChar().toString(),
                style = TextStyle(color = Color.White, fontSize = 18.sp)
            )
        }
    }
}