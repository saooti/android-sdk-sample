package com.saooti.sdksample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saooti.core.Saooti
import com.saooti.core.playing.models.PlayingStatus
import com.saooti.sdksample.ui.theme.SDKSampleTheme
import com.saooti.ui.SaootiUI
import com.saooti.ui.theme.SaootiUITheme
import com.saooti.ui.ui.bound.navigation.models.NavbarConfig
import com.saooti.ui.ui.bound.navigation.models.NavbarModeTitle
import com.saooti.ui.ui.bound.ui.views.UI
import com.saooti.ui.ui.elements.miniplayer.views.MiniPlayerView
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Saooti.init(this)

        Saooti.setApiUrl("https://api.octopus.saooti.com")
        Saooti.setOrganisationId("<organisation_id>")

        SaootiUI.bind()

        setContent {
            SDKSampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView()
                }
            }
        }
    }
}

@Composable
fun MainView() {

    val isSDKUIVisible = remember { mutableStateOf(false) }

    val isMiniPlayerVisible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Saooti.player.playingState
            .filterNotNull()
            .filter { it.playingStatus == PlayingStatus.Playing }
            .collect {
                isMiniPlayerVisible.value = true
            }
    }

    Box(
        contentAlignment = Alignment.Center
    ) {

        if (!isSDKUIVisible.value) {
            Button(
                onClick = { isSDKUIVisible.value = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Show Podcasts")
            }

            if (isMiniPlayerVisible.value) {

                //// Mini Player ////

                // You can keep mini player visible when SDK UI is not visible

                MiniPlayerView(
                    Modifier
                        .align(Alignment.BottomCenter),
                    isCloseButtonVisible = true,
                    onClick = {
                        isSDKUIVisible.value = true
                    },
                    onCloseButtonClick = {
                        isMiniPlayerVisible.value = false
                    }
                )
            }
        }

        AnimatedVisibility(
            isSDKUIVisible.value,
            modifier = Modifier.fillMaxSize(),
            enter = slideInVertically (
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            )
        ) {

            //// SDK UI ////

            SaootiUITheme {
                UI(
                    navbarConfig = NavbarConfig(
                        mode = NavbarModeTitle("Sample"),
                        onCloseButtonClick = {
                            isSDKUIVisible.value = false
                        }
                    )
                )
            }
        }
    }
}
