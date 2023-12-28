package com.saooti.sdksample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saooti.core.Saooti
import com.saooti.core.playing.models.PlayingStatus
import com.saooti.sdksample.ui.theme.SDKSampleTheme
import com.saooti.ui.R
import com.saooti.ui.SaootiUI
import com.saooti.ui.elements.broadcast.views.BroadcastHubView
import com.saooti.ui.elements.miniplayer.views.MiniPlayerView
import com.saooti.ui.elements.podcasts.main.views.PodcastsHubView
import com.saooti.ui.elements.podcasts.navigation.models.PodcastsNavbarConfig
import com.saooti.ui.theme.SaootiUITheme
import com.saooti.ui.theme.Theme
import com.saooti.ui.theme.ThemeModeValue
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SaootiUI.bind()

        Theme.Podcasts.NavBarView.backgroundColor = ThemeModeValue(default = Color.White)
        Theme.Podcasts.NavBarView.Home.view = ThemeModeValue(
            default = {
                Text(
                    "Podcasts UI",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
            }
        )

        Theme.Podcasts.HomeView.PodcastsView.isGroupByDateEnabled = false

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

    val isPodcastsUIVisible = remember { mutableStateOf(false) }
    val isBroadcastUIVisible = remember { mutableStateOf(false) }

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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "App environment"
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { isPodcastsUIVisible.value = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Podcasts UI")
            }

            Button(
                onClick = { isBroadcastUIVisible.value = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Broadcast UI")
            }
        }

        if (isMiniPlayerVisible.value) {
            MiniPlayerView(
                Modifier
                    .align(Alignment.BottomCenter),
                isCloseButtonVisible = true,
                onCloseButtonClick = {
                    isMiniPlayerVisible.value = false
                }
            )
        }

        AnimatedVisibility(
            isPodcastsUIVisible.value,
            modifier = Modifier.fillMaxSize(),
            enter = slideInVertically (
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            )
        ) {
            SaootiUITheme {
                PodcastsHubView(
                    navbarConfig = PodcastsNavbarConfig(
                        onCloseButtonClick = {
                            isPodcastsUIVisible.value = false
                        }
                    )
                )
            }
        }

        AnimatedVisibility(
            isBroadcastUIVisible.value,
            modifier = Modifier.fillMaxSize(),
            enter = slideInVertically (
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            )
        ) {
            Column {
                // Broadcast UI has no nav bar by default, so we can add one
                BroadcastUINavBarView(
                    onBackButtonClick = {
                        isBroadcastUIVisible.value = false
                    }
                )

                SaootiUITheme {
                    BroadcastHubView()
                }
            }
        }
    }
}

@Composable
fun BroadcastUINavBarView(
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit = {}
) {
    Box(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(15.dp),
        ) {
            Column(
                Modifier
                    .align(Alignment.CenterVertically)
                    .width(60.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(R.drawable.navbar_back_button),
                    null,
                    Modifier
                        .size(15.dp)
                        .clickable { onBackButtonClick() },
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color(120, 120, 120))
                )
            }

            Column(
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Broadcast UI",
                    style = TextStyle(
                        color = Color(60, 60, 60),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Left
                    )
                )
            }

            Column(
                Modifier
                    .width(60.dp)
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.End
            ) {

            }
        }
    }
}
