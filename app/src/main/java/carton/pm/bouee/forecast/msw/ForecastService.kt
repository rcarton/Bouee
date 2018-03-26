package carton.pm.bouee.forecast.msw

import android.util.Log
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.javanet.NetHttpTransport

val HTTP_TRANSPORT = NetHttpTransport()

class ForecastService(private val forecastConfig: ForecastConfig) {

  private val objectMapper = jacksonObjectMapper()

  init {
    objectMapper.registerModule(JavaTimeModule())
  }

  fun getForecasts(spotId: Int): ForecastResponse {
    Log.d("ForecastService", "Loading forecast for spot=$spotId")

    // Get the forecast
    val spotUrl = "http://magicseaweed.com/api/${forecastConfig.apiKey}/forecast/?spot_id=$spotId"

    val requestFactory = HTTP_TRANSPORT.createRequestFactory()
    val request = requestFactory.buildGetRequest(GenericUrl(spotUrl))
    val forecastJson = request.execute().parseAsString()

    Log.d("ForecastService", "Forecast loaded.")

    // Deserialize
    val forecastResponse: ForecastResponse = objectMapper.readValue(forecastJson)

    Log.d("ForecastService", "Forecast response deserialized")

    return forecastResponse
  }
}