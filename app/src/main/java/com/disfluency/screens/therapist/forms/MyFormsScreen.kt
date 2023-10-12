package com.disfluency.screens.therapist.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.SearchBar
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.ListItem
import com.disfluency.components.thumbnail.TitleThumbnail
import com.disfluency.model.form.Form
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold

@Composable
fun MyFormsScreen(
    therapist: Therapist,
    navController: NavHostController
){
    val filterQuery = rememberSaveable { mutableStateOf("") }

    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Therapist.items(),
        navController = navController
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBar(filterQuery = filterQuery)

                FormList(forms = therapist.forms, navController = navController, filter = filterQuery.value)
            }

            FormCreationButton(navController = navController)
        }
    }
}

@Composable
private fun FormList(forms: List<Form>, navController: NavHostController, filter: String){
    val filteredList = forms.filter { it.title.contains(filter, true)}

    if (filteredList.isEmpty()){
        NoFormsMessage()
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredList) {
            FormListItem(form = it, navController = navController)
        }
    }
}

@Composable
private fun FormListItem(form: Form, navController: NavHostController){
    ListItem(
        title = form.title,
        leadingContent = {
            TitleThumbnail(form.title)
        },
        onClick = {
            navController.navigate(
                Route.Therapist.FormDetail.routeTo(form.id)
            )
        }
    )
}

@Composable
private fun NoFormsMessage(){
    ImageMessagePage(imageResource = R.drawable.form_fill, text = stringResource(R.string.doesnt_have_forms_in_system))
}

@Composable
private fun FormCreationButton(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(Route.Therapist.NewForm.path)
            },
            modifier = Modifier.padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, stringResource(id = R.string.create))
        }
    }
}