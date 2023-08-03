package com.disfluency.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.disfluency.R
import com.disfluency.components.animation.DisfluencyAnimatedLogoRise
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.LoggedUserViewModel


val LOGO_OFFSET = 120.dp

@Composable
fun DisfluencyLaunchScreen(
    navController: NavController,
    viewModel: LoggedUserViewModel = viewModel()
){
    /*
    TODO: mientras carga el logo, deberiamos hacer el chequeo de agarran nuestro token de sesion
     guardado en el telefono y pegarle al back para que valide si ya estamos loggeados, y ahi directamente
     ni bien termina la animacion lo mandamos al home.
     */
    val animationState = remember { mutableStateOf(false) }

    //TODO: hacer que no haga la animacion del logo una vez que vuelvo a esta pantalla
    Box(modifier = Modifier.fillMaxSize()){
        AnimatedVisibility(visible = animationState.value, enter = fadeIn(animationSpec = tween(delayMillis = 500))) {
            LaunchScreenContent(navController = navController)
        }
        DisfluencyAnimatedLogoRise(animationState = animationState, riseOffset = LOGO_OFFSET)
    }
}

@Composable
private fun LaunchScreenContent(
    navController: NavController
){
    val logInAction = {
        navController.navigate(Route.Login.path)
    }

    val signUpAction = {
        navController.navigate(Route.SignUpLobby.path)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(LOGO_OFFSET))

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = "Facilitá el tratamiento de la disfluencia",
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            modifier = Modifier.width(250.dp),
            onClick = logInAction
        ) {
            Text(text = "Ingresar")
        }

        OutlinedButton(
            modifier = Modifier.width(250.dp),
            onClick = signUpAction
        ) {
            Text(text = stringResource(id = R.string.signup))
        }
    }
}