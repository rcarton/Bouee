package carton.pm.bouee.forecast.quickswell

import carton.pm.bouee.forecast.AbstractForecastTest
import org.junit.Assert
import org.junit.Test

class ForecastTest(): AbstractForecastTest() {

  @Test
  fun deserialization_ok() {

    Assert.assertEquals("2018-03-17T06:00:00Z", forecast?.timestamp)
  }
}
