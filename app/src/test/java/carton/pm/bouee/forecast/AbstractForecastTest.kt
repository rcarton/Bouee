package carton.pm.bouee.forecast

import carton.pm.bouee.forecast.msw.Forecast
import carton.pm.bouee.forecast.msw.ForecastTest
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.BeforeClass

abstract class AbstractForecastTest {
  companion object {
    var forecastJson: String = ""
    val mapper = jacksonObjectMapper()
    var forecasts = emptyArray<Forecast>()

    @BeforeClass
    @JvmStatic
    fun setup() {
      mapper.registerModule(JavaTimeModule())
      forecastJson = ForecastTest::class.java.getResource("/forecast/msw/forecast_response.json").readText()
      forecasts = mapper.readValue(forecastJson)
    }
  }

}