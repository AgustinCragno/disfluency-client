package com.disfluency.navigation.routing

import com.disfluency.R

sealed class Route(val path: String, val title: Int) {

    object Login: Route("login", R.string.login)
}