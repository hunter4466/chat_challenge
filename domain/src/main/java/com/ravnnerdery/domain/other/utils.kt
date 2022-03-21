package com.ravnnerdery.domain.other

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun timeConvertor(time: Long): String {
    val simple = SimpleDateFormat("dd MMM yyyy HH:mm")
    val result = Date(time)
    return simple.format(result)
}