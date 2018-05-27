package carton.pm.bouee.forecast.quickswell

import java.time.Instant

data class ForecastResponse(
    val forecastTime: Instant,
    val utcOffsetMinutes: Int,
    val forecasts: Array<Forecast>
)

