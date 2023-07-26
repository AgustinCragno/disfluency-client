package com.disfluency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.disfluency.navigation.AppNavigation
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.PropertiesReader
import com.disfluency.utilities.avatar.AvatarManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            PropertiesReader.initialize(applicationContext)
            AvatarManager.initialize(applicationContext)
        }

        setContent {
            DisfluencyTheme {
                AppNavigation()
            }
        }
    }
}
