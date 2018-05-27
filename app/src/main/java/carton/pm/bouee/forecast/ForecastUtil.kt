package carton.pm.bouee.forecast

import carton.pm.bouee.forecast.quickswell.Forecast
import carton.pm.bouee.forecast.quickswell.ForecastResponse
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * Figure out the number of days in a list of forecasts
 */
fun getNumberOfDaysInForecast(forecastResponse: ForecastResponse): Int {
  if (forecastResponse.forecasts.size < 2) {
    return forecastResponse.forecasts.size
  }
  val utcOffsetMinutes = forecastResponse.utcOffsetMinutes
  return forecastResponse.forecasts.fold<Forecast, Set<LocalDate>>(emptySet<LocalDate>(), { acc: Set<LocalDate>, forecast: Forecast -> acc.plusElement(getLocalDayOfForecast(forecast, utcOffsetMinutes)) }).size
}

fun getLocalDayOfForecast(forecast: Forecast, utcOffsetMinutes: Int): LocalDate {
  return LocalDate.from(forecast.timestamp.minus(utcOffsetMinutes.toLong(), ChronoUnit.MINUTES).atZone(ZoneId.of("GMT")))
}

