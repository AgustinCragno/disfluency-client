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
import com.disfluency.components.skeleton.SKELETON_BACKGROUND
import com.disfluency.components.skeleton.SKELETON_CONTENT
import com.disfluency.ui.theme.DisfluencyTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun ExerciseAssignmentListItemSkeleton(){
    Card(
        modifier = Modifier.shimmer(),
        colors = CardDefaults.cardColors(containerColor = SKELETON_BACKGROUND, contentColor = SKELETON_BACKGROUND)
    ){
        ListItem(
            modifier = Modifier.height(56.dp),
            colors = ListItemDefaults.colors(containerColor = SKELETON_BACKGROUND),
            headlineContent = {
                Box(modifier = Modifier
                    .width(150.dp)
                    .height(20.dp)
                    .padding(bottom = 2.dp)
                    .background(SKELETON_CONTENT)
                )
            },
            supportingContent = {
                Box(modifier = Modifier
                    .width(70.dp)
                    .height(20.dp)
                    .padding(top = 2.dp)
                    .background(SKELETON_CONTENT)
                )
            },
            leadingContent = {
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(SKELETON_CONTENT)
                )
            },
            trailingContent = {
                Box(modifier = Modifier
                    .width(80.dp)
                    .height(20.dp)
                    .background(SKELETON_CONTENT)
                )
            }
        )
    }
}

@Composable
fun ExerciseAssignmentListSkeleton(){
    val defaultCount = 5
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(defaultCount) {
            ExerciseAssignmentListItemSkeleton()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseAssignmentListItemSkeletonComparison(){

    DisfluencyTheme() {
        Column(Modifier.padding(16.dp)) {
//            ExerciseAssignmentListItem(MockedData.assignments[0], rememberNavController())

            Spacer(modifier = Modifier.padding(8.dp))

            ExerciseAssignmentListItemSkeleton()
        }
    }
}