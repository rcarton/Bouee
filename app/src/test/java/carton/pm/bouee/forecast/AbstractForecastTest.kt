package carton.pm.bouee.forecast

import carton.pm.bouee.forecast.quickswell.ForecastResponse
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

abstract class AbstractForecastTest {

  var forecastJson: String = ""
  val mapper = jacksonObjectMapper()
  val forecastResponse: ForecastResponse

  init {
    mapper.registerModule(JavaTimeModule())
    forecastJson = AbstractForecastTest::class.java.getResource("/forecast/forecast_response.json").readText()
    forecastResponse = mapper.readValue(forecastJson)
  }
}