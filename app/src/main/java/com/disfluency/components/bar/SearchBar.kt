package com.disfluency.components.bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.disfluency.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(filterQuery: MutableState<String>){
    val focusManager = LocalFocusManager.current

    androidx.compose.material3.SearchBar(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 8.dp),
        query = filterQuery.value,
        onQueryChange = { filterQuery.value = it },
        onSearch = { focusManager.clearFocus() },
        active = false,
        onActiveChange = { },
        placeholder = { Text(stringResource(R.string.search)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
    ) {}
}