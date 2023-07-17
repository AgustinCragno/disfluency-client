package com.disfluency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.disfluency.navigation.AppNavigation
import com.disfluency.ui.theme.DisfluencyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisfluencyTheme {
                AppNavigation()
            }
        }
    }
}
