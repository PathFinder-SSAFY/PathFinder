package com.dijkstra.pathfinder.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainScreenBottomIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
    ) {
        content()
    }
}