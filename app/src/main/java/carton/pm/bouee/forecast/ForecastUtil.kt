package carton.pm.bouee.forecast

import carton.pm.bouee.forecast.msw.Forecast
import java.time.LocalDate
import java.time.ZoneId

/**
 * Figure out the number of days in a list of forecasts
 */
fun getNumberOfDaysInForecast(forecasts: Array<Forecast>): Int {
  if (forecasts.size < 2) {
    return forecasts.size
  }
  return forecasts.fold<Forecast, Set<LocalDate>>(emptySet<LocalDate>(), { acc: Set<LocalDate>, forecast: Forecast -> acc.plusElement(getLocalDayOfForecast(forecast)) }).size
}

fun getLocalDayOfForecast(forecast: Forecast): LocalDate {
  val secDiff = (forecast.timestamp.epochSecond - forecast.localTimestamp.epochSecond)
  return LocalDate.from(forecast.timestamp.minusSeconds(secDiff).atZone(ZoneId.of("GMT")))
}

//fun getWaveHeight(waveHeight: Float, maxHeight) {
//
//}
