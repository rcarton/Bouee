package carton.pm.bouee.forecast.quickswell

import java.time.Instant

data class Forecast(
    val swell: Swell,
    val wind: Wind,
    val timestamp: Instant
)
