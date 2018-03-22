package carton.pm.bouee.forecast.msw

import carton.pm.bouee.forecast.AbstractForecastTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ForecastTest(): AbstractForecastTest() {
  @Test
  fun deserialization_ok() {

    assertEquals("2018-03-17T06:00:00Z", forecasts[0].timestamp.toString())
  }
}