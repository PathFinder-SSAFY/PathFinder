package com.dijkstra.pathfinder.screen.splash

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import com.dijkstra.pathfinder.R

@Preview(showBackground = true)
@Composable
fun SplashScreen() {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000,
            easing = LinearOutSlowInEasing
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        // TODO : Navigate To Home Screen
//        navController.popBackStack()
//        navController.navigate(Screen.Home.route)
    }

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_main_logo_big),
                contentDescription = "App Main Logo",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth().alpha(alphaAnimation.value)
            )
        }
    }
}