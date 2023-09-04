package com.disfluency.screens.therapist.analysis.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun StatsPanel(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
){
    Card(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            Row(
                modifier = Modifier
                    .background(Color.LightGray.copy(alpha = 0.15f))
                    .fillMaxWidth()
            ) {
                val fontSize = 18.sp
                val padding = 12.dp

                Text(
                    text = title,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize,
                    modifier = Modifier.padding(padding)
                )
            }

            Divider(
                thickness = 3.dp,
                color = Color.Gray.copy(alpha = 0.3f)
            )

            content()
        }
    }
}