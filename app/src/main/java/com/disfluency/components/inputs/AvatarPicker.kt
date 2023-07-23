package com.disfluency.components.inputs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.utilities.avatar.AvatarManager


@Composable
fun AvatarPicker(selectedAvatarIndex: MutableState<Int>){
    val availableIndices = AvatarManager.getIndices()

    var internalIndex by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            modifier = Modifier.size(60.dp),
            onClick = {
                internalIndex--
                selectedAvatarIndex.value = availableIndices[internalIndex]
            },
            enabled = internalIndex > 0,
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary, disabledContentColor = Color.Gray)
        ) {
            Icon(imageVector = Icons.Filled.NavigateBefore, contentDescription = "Previous")
        }

        Image(
            modifier = Modifier
                .size(90.dp),
            painter = painterResource(id = AvatarManager.getAvatarId(availableIndices[internalIndex])),
            contentDescription = "Avatar"
        )

        IconButton(
            modifier = Modifier.size(60.dp),
            onClick = {
                internalIndex++
                selectedAvatarIndex.value = availableIndices[internalIndex]
            },
            enabled = internalIndex < availableIndices.size - 1,
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary, disabledContentColor = Color.Gray)
        ) {
            Icon(imageVector = Icons.Filled.NavigateNext, contentDescription = "Next")
        }
    }
}