package com.saooti.sdksample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.saooti.core.Saooti
import com.saooti.sdksample.ui.theme.SDKSampleTheme
import com.saooti.ui.SaootiUI
import com.saooti.ui.theme.SaootiUITheme
import com.saooti.ui.ui.bind.ui.views.UI


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Saooti.init(this)
        Saooti.setApiUrl("https://api.octopus.saooti.com")
        Saooti.setOrganizationId("<organization_id>")

        SaootiUI.bind()

        setContent {
            SDKSampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SaootiUITheme {
                        UI()
                    }
                }
            }
        }
    }
}
