package com.disfluency.components.skeleton.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.disfluency.components.list.item.PatientListItem
import com.disfluency.components.skeleton.SKELETON_BACKGROUND
import com.disfluency.components.skeleton.SKELETON_CONTENT
import com.disfluency.ui.theme.DisfluencyTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun PatientListItemSkeleton(){
    Card(
        modifier = Modifier.shimmer(),
        colors = CardDefaults.cardColors(containerColor = SKELETON_BACKGROUND, contentColor = SKELETON_BACKGROUND)
    ) {
        ListItem(
            colors = ListItemDefaults.colors(containerColor = SKELETON_BACKGROUND),
            modifier = Modifier
                .height(56.dp),
            headlineContent = {
                Box(modifier = Modifier
                    .width(200.dp)
                    .height(20.dp)
                    .padding(bottom = 2.dp)
                    .background(SKELETON_CONTENT))
            },
            supportingContent = {
                Box(modifier = Modifier
                    .width(90.dp)
                    .height(20.dp)
                    .padding(top = 2.dp)
                    .background(SKELETON_CONTENT))
            },
            leadingContent = {
                Box(modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SKELETON_CONTENT))
            },
            trailingContent = {
                Box(modifier = Modifier
                    .width(55.dp)
                    .height(25.dp)
                    .background(SKELETON_CONTENT))
            }
        )
    }
}

@Composable
fun PatientListSkeleton(){
    val defaultCount = 5
    LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(defaultCount) {
            PatientListItemSkeleton()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PatientListItemSkeletonComparison(){

    DisfluencyTheme() {
        Column(Modifier.padding(16.dp)) {
//            PatientListItem(patient = MockedData.patients[0])

            Spacer(modifier = Modifier.padding(8.dp))

            PatientListItemSkeleton()
        }
    }
}