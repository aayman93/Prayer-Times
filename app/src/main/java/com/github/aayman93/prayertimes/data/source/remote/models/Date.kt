package com.github.aayman93.prayertimes.data.source.remote.models

data class Date(
    val gregorian: Gregorian,
    val readable: String,
    val timestamp: String
)