package com.dijkstra.pathfinder.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dijkstra.pathfinder.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val nanumSquareNeo = FontFamily(
    Font(R.font.nanum_square_neo_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.nanum_square_neo_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.nanum_square_neo_regular, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.nanum_square_neo_light, FontWeight.Light, FontStyle.Normal),
)