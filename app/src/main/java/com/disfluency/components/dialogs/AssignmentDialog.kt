package com.disfluency.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.components.button.SendAndCancelButtons
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.SelectablePatientListItem
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.PatientListSkeleton
import com.disfluency.model.user.Patient


@Composable
fun <T>AssignmentDialog(
    contentState: MutableState<T?>,
    content: @Composable () -> Unit,
    dismissAction: () -> Unit,
    onSend: () -> Unit,
    sendEnabled: Boolean = true,
    onLaunch: () -> Unit
){
    LaunchedEffect(Unit){
        onLaunch()
    }

    AnimatedDialog(dismissAction = dismissAction) {
        Surface(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = stringResource(R.string.assign_to),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(10f)
                ) {
                    SkeletonLoader(
                        state = contentState,
                        content = content,
                        skeleton = {
                            PatientListSkeleton()
                        }
                    )
                }

                SendAndCancelButtons(
                    modifier = Modifier.weight(1f),
                    onCancel = dismissAction,
                    sendEnabled = sendEnabled,
                    onSend = onSend
                )
            }
        }
    }
}
