package com.disfluency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.disfluency.navigation.AppNavigation
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.PropertiesReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PropertiesReader.initialize(applicationContext)

        setContent {
            DisfluencyTheme {
                AppNavigation()
            }
        }
    }
}
