package carton.pm.bouee.forecast.msw

import java.time.Instant

data class Forecast(
  // Timestamp
    val timestamp: Instant,
    val localTimestamp: Instant,
    val issueTimestamp: Instant,

  // Rating
    val fadedRating: Int,
    val solidRating: Int,

    val swell: Swell,
    val wind: Wind,
    val condition: Condition,
    val charts: Charts
)
