package carton.pm.bouee.forecast

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.util.Log
import carton.pm.bouee.R
import carton.pm.bouee.forecast.msw.ForecastConfig
import carton.pm.bouee.forecast.msw.ForecastService

const val ACTION_FORECAST_LOADED = "carton.pm.bouee.action.FORECAST_LOADED"

const val EXTRA_FORECAST_PAYLOAD_ID = "forecastLoaded"
const val EXTRA_FORECAST_SPOT_ID = "forecastSpotId"
const val EXTRA_FORECAST_SPOT_NAME = "forecastSpotName"

const val JOB_ID = 1000

fun enqueueWork(context: Context, work: Intent) {
  Log.d(ForecastIntentService::class.toString(), "static enqueueWork")
  JobIntentService.enqueueWork(context, ForecastIntentService::class.java, 1000, work)
}

class ForecastIntentService: JobIntentService() {

  override fun onHandleWork(intent: Intent) {
    val widgetId = intent.extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
    val spotId = intent.getIntExtra(EXTRA_FORECAST_SPOT_ID, 384)
    val spotName = intent.getStringExtra(EXTRA_FORECAST_SPOT_NAME)

    // TODO move this out
    val forecastConfig = ForecastConfig(apiKey = baseContext.getString(R.string.msw_key))
    val forecastService = ForecastService(forecastConfig)

    Log.d(ForecastIntentService()::class.toString(), "IntentService onHandleIntent widgetId=$widgetId spotId=$spotId")

    try {
      val payload = forecastService.getForecastsAsString(spotId)

      val forecastLoadedIntent = Intent()
      forecastLoadedIntent.putExtra(EXTRA_FORECAST_PAYLOAD_ID, payload)
      forecastLoadedIntent.putExtra(EXTRA_FORECAST_SPOT_ID, spotId)
      forecastLoadedIntent.putExtra(EXTRA_FORECAST_SPOT_NAME, spotName)
      forecastLoadedIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
      forecastLoadedIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

      // TODO draw bitmap and store in the app storage as a png for that spot
      // then this could be used instead of loading

      sendBroadcast(forecastLoadedIntent);
    } catch (t: Throwable) {
      Log.e(ForecastIntentService::class.toString(), "Error while fetching forecast", t)

      // TODO send error broadcast
    }
  }
}