package com.dijkstra.pathfinder.components

import androidx.compose.material.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dijkstra.pathfinder.R
import com.dijkstra.pathfinder.ui.theme.IconColor
import com.dijkstra.pathfinder.ui.theme.nanumSquareNeo

@Composable
fun MainScreenTextButton(
    onClick: () -> Unit,
    text: String
) {
    TextButton(
        onClick = onClick
    ) {
        Text(
            text = text,
            fontFamily = nanumSquareNeo,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = IconColor
        )
    }
}