package com.disfluency.screens.therapist.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.R
import com.disfluency.utilities.color.verticalGradientStrong

@Composable
fun ShortcutButton(
    modifier: Modifier,
    title: String,
    background: Int,
    onClick: () -> Unit) {

    Box(modifier = modifier
        .height(90.dp)
        .clip(RoundedCornerShape(16.dp))
        .clickable { onClick() }
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = background),
            contentDescription = "User Role Thumbnail",
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    verticalGradientStrong(Color.Black)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "crea un nuevo",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )

            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontSize = 21.sp
            )
        }
    }
}