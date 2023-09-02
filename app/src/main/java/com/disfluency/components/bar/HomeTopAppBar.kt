package com.disfluency.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.dialogs.ExitDialog
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.LoggedUserViewModel

@Composable
fun HomeTopAppBar(navController: NavHostController, viewModel: LoggedUserViewModel){
    var openLogoutDialog by remember { mutableStateOf(false) }

    var executeLogoutRoutine by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.surface)
    ){
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = { openLogoutDialog = true }
        ) {
            Icon(
                imageVector = Icons.Outlined.ExitToApp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    if (openLogoutDialog){
        ExitDialog(
            onAccept = { executeLogoutRoutine = true },
            onCancel = { openLogoutDialog = false }
        )
    }

    if (executeLogoutRoutine){
        LaunchedEffect(Unit){
            viewModel.exitToLaunch()
        }
    }
}