package com.dijkstra.pathfinder.screen.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dijkstra.pathfinder.R

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current

    Surface(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_main_logo_big),
                contentDescription = "App Main Logo",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            Image(
                painter = painterResource(id = R.drawable.naver_login_button),
                contentDescription = "Naver Login Button",
                contentScale = ContentScale.Fit,
                modifier = Modifier.clickable {
                    Log.d("SDR_CLICK", "LoginScreen: click")
                    viewModel.startNaverLogin(context)
                }
            )
            Text(
                text = "LOGOUT",
                modifier = Modifier.clickable {
                    Log.d("SDR_CLICK", "LoginScreen: logout")
                    viewModel.startNaverLogout()
                }
            )

//            Text(
//                text = "Status ${viewModel.loginStatus.value}",
//                modifier = Modifier
//            )
//            Text(
//                text = if (viewModel.naverToken.value.isBlank()) {
//                    "No Token"
//                } else {
//                    "Token ${viewModel.naverToken.value}"
//                }
//            )
        }
    }
}