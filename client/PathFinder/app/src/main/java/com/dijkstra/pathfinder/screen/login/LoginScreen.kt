package com.dijkstra.pathfinder.screen.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dijkstra.pathfinder.R

@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    Surface(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceAround   ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_main_logo),
                contentDescription = "App Main Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.naver_login_button),
                contentDescription = "Naver Login Button",
                contentScale = ContentScale.Fit,
                modifier = Modifier.clickable {
                    Log.d("SDR_CLICK", "LoginScreen: click")
                }
            )
        }
    }
}