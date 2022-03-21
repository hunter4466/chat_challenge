package com.ravnnerdery.chat_challenge.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

val ApplicationTypography by lazy {
    val typography = Typography(
        h1 = TextStyle(
            fontSize = 42.sp
        ),
        h2 = TextStyle(
            fontSize = 38.sp
        ),
        h3 = TextStyle(
            fontSize = 34.sp
        ),
        h4 = TextStyle(
            fontSize = 30.sp
        ),
        h5 = TextStyle(
            fontSize = 26.sp
        ),
        h6 = TextStyle(
            fontSize = 22.sp
        ),
        subtitle1 = TextStyle(
            fontSize = 16.sp
        ),
        subtitle2 = TextStyle(
            fontSize = 14.sp
        ),
        body1 = TextStyle(
            fontSize = 12.sp
        ),
        body2 = TextStyle(
            fontSize = 10.sp
        ),
        button = TextStyle(
            fontSize = 14.sp
        ),
        caption = TextStyle(
            fontSize = 12.sp
        ),
        overline = TextStyle(
            fontSize = 12.sp
        )
    )
    typography
}