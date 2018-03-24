package carton.pm.bouee.forecast.msw

import com.google.api.client.http.javanet.NetHttpTransport

val HTTP_TRANSPORT = NetHttpTransport()
val JSON_FACTORY = JacksonFactory()
val FORECAST_URL = "http://magicseaweed.com/api/${apiKey}/forecast/?spot_id=466"

class ForecastService {

  fun getForecasts(spotId: Int): ForecastResponse {

  }

  fun getForecastUrl(spotId: Int): {

  }
}