package carton.pm.bouee.forecast

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class ForecastUtilTest(): AbstractForecastTest() {

  @Test
  fun getNumberOfDaysInForecast_ok() {
    // By default it's 40 Forecast objects 3 hours apart, so it's 5 days
    assertEquals(5, getNumberOfDaysInForecast(forecastResponse))
  }

  @Test
  fun getLocalDayOfForecast_differentDays() {
    val forecasts = forecastResponse.forecasts
    // That's a Saturday
    val ts = Instant.from(ZonedDateTime.of(1985, 5, 25, 2, 0, 0, 0, ZoneId.of("GMT")))

    val forecast = forecasts[0].copy(timestamp = ts)
    val localDay = getLocalDayOfForecast(forecast, forecastResponse.copy(utcOffsetMinutes = -240).utcOffsetMinutes)

    assertEquals(DayOfWeek.FRIDAY, localDay.dayOfWeek)
  }

  @Test
  fun getLocalDayOfForecast_sameDay() {
    val forecasts = forecastResponse.forecasts

    // That's a Saturday
    val ts = Instant.from(ZonedDateTime.of(1985, 5, 25, 2, 0, 0, 0, ZoneId.of("GMT")))

    val forecast = forecasts[0].copy(timestamp = ts)
    val localDay = getLocalDayOfForecast(forecast, forecastResponse.copy(utcOffsetMinutes = -240).utcOffsetMinutes)

    assertEquals(DayOfWeek.SATURDAY, localDay.dayOfWeek)
  }
}