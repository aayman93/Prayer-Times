package com.github.aayman93.prayertimes.data.source.remote.models.qibla

data class QiblaResponse(
    val code: Int,
    val `data`: Qibla,
    val status: String
)