package com.disfluency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.disfluency.api.DisfluencyAPI
import com.disfluency.api.session.SessionManager
import com.disfluency.navigation.AppNavigation
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.PropertiesReader
import com.disfluency.utilities.avatar.AvatarManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            0
        )

        lifecycleScope.launch {
            SessionManager.initialize(applicationContext)
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
