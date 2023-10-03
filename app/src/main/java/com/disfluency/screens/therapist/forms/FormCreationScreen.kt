package com.disfluency.screens.therapist.forms

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.api.dto.NewFormDTO
import com.disfluency.components.inputs.text.CleanLabeledTextField
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.utilities.color.mix
import com.disfluency.viewmodel.FormCreationViewModel
import kotlinx.coroutines.delay
import kotlin.math.min


@Composable
fun FormCreationScreen(
    therapist: Therapist,
    navController: NavHostController,
    viewModel: FormCreationViewModel
){
    BackNavigationScaffold(
        title = stringResource(R.string.new_form),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FormQuestionCreationList(
                therapist = therapist,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun FormQuestionCreationList(
    therapist: Therapist,
    navController: NavHostController,
    viewModel: FormCreationViewModel
){
    val title = remember {
        mutableStateOf("")
    }

    val questions = remember {
        mutableStateListOf<FormQuestionCreation>()
    }

    val questionsAreSubmittable = {
        questions.isNotEmpty() && questions.all { it.isComplete() }
    }

    val questionsCount = remember {
        derivedStateOf { questions.size }
    }

    var submitted by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(9f),
            state = listState
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    //TODO: que no te deje borrarlo del todo una vez que ya lo llenaste, osea que no te
                    // deje salir del teclado
                    CleanLabeledTextField(
                        input = title,
                        label = stringResource(id = R.string.title),
                        placeholder = stringResource(R.string.enter_title),
                        textStyle = MaterialTheme.typography.displayMedium,
                        fontSize = 22.sp,
                        onSubmit = {
                            if (questions.isEmpty()) {
                                questions.add(
                                    FormQuestionCreation(1)
                                )
                            }
                        }
                    )
                }
            }

            questions.forEach {
                item {
                    FormQuestionCreation(questionCreation = it)
                }
            }

            item {
                if (questionsAreSubmittable())
                    AddButtonDivider(list = questions)

                Spacer(modifier = Modifier.height(400.dp))
            }
        }

        SubmitCancelButtons(
            modifier = Modifier.weight(1f),
            sendEnabled = title.value.isNotBlank() && questionsAreSubmittable(),
            onSubmit = {
                viewModel.createFormForTherapist(
                    therapist = therapist,
                    newFormDTO = NewFormDTO.from(title.value, questions.toList())
                )
                submitted = true
            },
            onCancel = {
                navController.popBackStack()
            }
        )
    }

    LaunchedEffect(key1 = questionsCount.value){
        scrollToNewestFormItem(listState, questionsCount.value)
    }

    if (submitted){
        LaunchedEffect(Unit){
            delay(200)
            navController.popBackStack()
            navController.navigate(Route.Therapist.ConfirmationNewForm.path)
        }
    }
}

private suspend fun scrollToNewestFormItem(
    listState: LazyListState,
    questionsCount: Int
){
    if (questionsCount > 1){
        val targetOffset = listState.layoutInfo.visibleItemsInfo
            .find { it.index == questionsCount }?.offset

        targetOffset?.let {
            listState.animateScrollBy(
                value = it.toFloat(),
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FormQuestionCreation(
    questionCreation: FormQuestionCreation
){
    val slider = remember { mutableStateOf(3f) }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        val (b, c, d, e) = FocusRequester.createRefs()

        //TODO: boton para borrar
        NumberedDivider(number = questionCreation.number)

        CleanLabeledTextField(
            input = questionCreation.question,
            label = stringResource(id = R.string.question),
            placeholder = stringResource(id = R.string.enter_scale_question),
            fontSize = 16.sp,
            modifier = Modifier.focus(b, c)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val p1 = min((slider.value * 5f - 5f) / 10f, 1f)

            CleanLabeledTextField(
                input = questionCreation.scaleStart,
                label = stringResource(id = R.string.scale_start),
                placeholder = stringResource(id = R.string.start),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .focus(c, d),
                inputColor = MaterialTheme.colorScheme.primary
                    .mix(color = Color.Black, percentage = mixQuad(p1))
            )

            val p2 = min((5f - slider.value) / 2f, 1f)

            CleanLabeledTextField(
                input = questionCreation.scaleEnd,
                label = stringResource(id = R.string.scale_end),
                placeholder = stringResource(id = R.string.end),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .focus(d, e),
                inputColor = MaterialTheme.colorScheme.primary
                    .mix(color = Color.Black, percentage = mixQuad(p2))
            )
        }

//        Spacer(modifier = Modifier.height(4.dp))
//
//        ScaleSlider(state = slider)

        Spacer(modifier = Modifier.height(8.dp))

        CleanLabeledTextField(
            input = questionCreation.followUp,
            label = stringResource(id = R.string.follow_up_question),
            placeholder = stringResource(id = R.string.enter_follow_up_question),
            fontSize = 16.sp,
            keyboardAction = ImeAction.Done,
            modifier = Modifier.focusRequester(e)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LaunchedEffect(Unit){
            b.requestFocus()
        }
    }
}

@Composable
private fun NumberedDivider(number: Int){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Divider(
            color = MaterialTheme.colorScheme.secondary
        )

        Box(
            modifier = Modifier.width(40.dp),
            contentAlignment = Alignment.CenterEnd
        ){
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ){
                Box(modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                ){
                    Text(
                        text = number.toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun AddButtonDivider(list: MutableList<FormQuestionCreation>){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ){
        val color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)

        Divider(color = color)

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            IconButton(
                onClick = {
                    list.add(FormQuestionCreation(list.size + 1))
                },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Composable
private fun SubmitCancelButtons(
    modifier: Modifier = Modifier,
    sendEnabled: Boolean,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        TextButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            onClick = onCancel,
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }

        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            onClick = onSubmit,
            enabled = sendEnabled,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = stringResource(id = R.string.confirm))
        }
    }
}


private fun mixQuad(p: Float): Float{
    return p - (1 - p) * p * 1.25f
}

private fun Modifier.focus(thisOne: FocusRequester, nextOne: FocusRequester): Modifier {
    return this
        .focusRequester(thisOne)
        .focusProperties {
            next = nextOne
        }
}

class FormQuestionCreation(
    val number: Int
){
    val question: MutableState<String> = mutableStateOf("")
    val scaleStart: MutableState<String> = mutableStateOf("")
    val scaleEnd: MutableState<String> = mutableStateOf("")
    val followUp: MutableState<String> = mutableStateOf("")

    fun isComplete(): Boolean {
        return listOf(question, scaleStart, scaleEnd, followUp).all { it.value.isNotBlank() }
    }
}