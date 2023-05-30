package com.github.aayman93.prayertimes.util

import java.text.SimpleDateFormat
import java.util.Locale

fun String.convertTimeFormat(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val time = timeFormat.parse(this)
    val newFormat = SimpleDateFormat("hh:mm aaa", Locale.getDefault())
    return newFormat.format(time!!)
}

fun String.parseTime(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val time = timeFormat.parse(this)
    return timeFormat.format(time!!)
}