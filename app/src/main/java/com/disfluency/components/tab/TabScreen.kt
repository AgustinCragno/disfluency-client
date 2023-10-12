package com.disfluency.components.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.disfluency.components.icon.TextTag
import com.disfluency.utilities.color.darken


@Composable
fun TabScreen(tabs: List<TabItem>) {
    var tabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.secondary,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[tabIndex])
                        .height(4.dp)
                        .padding(horizontal = 28.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    selectedContentColor = MaterialTheme.colorScheme.secondary,
                    unselectedContentColor = Color.Gray,
                    icon = {
                        TabRowContent(tab = tab, isSelected = tabIndex == index)
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1f)){
            tabs[tabIndex].content()
        }
    }
}

@Composable
private fun TabRowContent(tab: TabItem, isSelected: Boolean){
    Row(modifier = Modifier.height(20.dp)) {
        Icon(
            imageVector = if (isSelected) tab.iconOn else tab.iconOff,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = tab.title,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Gray
        )

        tab.numberBadge?.let {
            Spacer(modifier = Modifier.width(6.dp))

            TextTag(
                text = it.toString(),
                color = MaterialTheme.colorScheme.secondary.darken(),
                offsetY = 2.dp
            )
        }
    }
}