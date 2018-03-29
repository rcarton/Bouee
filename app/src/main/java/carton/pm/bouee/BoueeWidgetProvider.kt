package carton.pm.bouee

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RemoteViews
import carton.pm.bouee.drawables.DrawableWidget
import carton.pm.bouee.forecast.EXTRA_FORECAST_PAYLOAD_ID
import carton.pm.bouee.forecast.EXTRA_FORECAST_SPOT_ID
import carton.pm.bouee.forecast.EXTRA_FORECAST_SPOT_NAME
import carton.pm.bouee.forecast.ForecastReceiver
import carton.pm.bouee.forecast.msw.ForecastConfig
import carton.pm.bouee.forecast.msw.ForecastResponse
import carton.pm.bouee.forecast.msw.ForecastService
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int,
                    spotId: Int, spotName: String) {
  val intent = Intent(context, ForecastReceiver::class.java)
  intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
  intent.putExtra(EXTRA_FORECAST_SPOT_ID, spotId)
  intent.putExtra(EXTRA_FORECAST_SPOT_NAME, spotName)

  val widget = RemoteViews(context.packageName, R.layout.bouee)

  // Show Loading
  widget.setTextViewText(R.id.placeholder, context.getString(R.string.loading))
  widget.setViewVisibility(R.id.placeholder, VISIBLE)

  val confirm = PendingIntent.getBroadcast(context, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  confirm.send()
}

class BoueeWidgetProvider() : AppWidgetProvider() {
  private val objectMapper = jacksonObjectMapper()

  init {
    objectMapper.registerModule(JavaTimeModule())
  }

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                        appWidgetIds: IntArray) {
    super.onUpdate(context, appWidgetManager, appWidgetIds)
    Log.d(BoueeWidgetProvider::class.toString(), "onUpdate")

    // For every widget
    appWidgetIds.forEach { appWidgetId ->
      Log.d(BoueeWidgetProvider::class.toString(), "Widget id=$appWidgetId")

      // Load the preferences
      val spotId = getPreferences(context, appWidgetId, PreferenceKeys.SPOT_ID).toInt()
      val spotName = getPreferences(context, appWidgetId, PreferenceKeys.SPOT_NAME)

      // Update the widget
      updateAppWidget(context, appWidgetManager, appWidgetId, spotId, spotName)
    }
  }

  override fun onReceive(context: Context, intent: Intent) {
    // If this never gets called, try creating a separate receiver instead.
    super.onReceive(context, intent)
    Log.d(BoueeWidgetProvider::class.toString(), "BoueeWidgetProvider.onReceive action=${intent.action}")

    // Yeah this is a rogue update.
    val action = intent.action
    if (AppWidgetManager.ACTION_APPWIDGET_UPDATE == action) {
      val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
      val payload: String? = intent.getStringExtra(EXTRA_FORECAST_PAYLOAD_ID)
      payload ?: return

      val spotName = intent.getStringExtra(EXTRA_FORECAST_SPOT_NAME)

      Log.i(BoueeWidgetProvider::class.toString(), "widgetId=$widgetId payload=$payload")

      // Deserialize the payload
      val result: ForecastResponse = objectMapper.readValue(payload, ForecastResponse::class.java)

      // Update this widget only
      val widget = RemoteViews(context.packageName, R.layout.bouee)

      // Hide Loading, show status bar
      widget.setViewVisibility(R.id.placeholder, GONE)

      // Render the chart
      widget.setImageViewBitmap(R.id.canvas, createRenderedBitmap(result, spotName))

      Log.d(BoueeWidgetProvider::class.toString(), "Updating widget.")
      AppWidgetManager.getInstance(context).updateAppWidget(widgetId, widget)
    }
  }

  private fun createRenderedBitmap(forecasts: ForecastResponse, spotName: String): Bitmap {
    Log.d(BoueeWidgetProvider::class.toString(), "Creating forecast bitmap")
    val bitmap = Bitmap.createBitmap(1080, 1080/5, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val drawable = DrawableWidget(forecasts, spotName)
    drawable.draw(canvas)

    return bitmap
  }

  private inner class RetrieveForecast(private val widgetId: Int,
                                       private val context: Context,
                                       private val appWidgetManager: AppWidgetManager) :
      AsyncTask<Int, Void, ForecastResponse>() {

    val widget = RemoteViews(context.packageName, R.layout.bouee)
    var exception: Exception? = null

    override fun onPreExecute() {
      super.onPreExecute()

      // Show Loading
      widget.setTextViewText(R.id.placeholder, context.getString(R.string.loading))
      widget.setViewVisibility(R.id.placeholder, VISIBLE)
    }

    override fun onPostExecute(result: ForecastResponse) {
//      super.onPostExecute(result)
//
//      // Handle errors
//      if (this.exception != null) {
//        Log.e(BoueeWidgetProvider::class.toString(), this.exception.toString())
//        widget.setTextViewText(R.id.placeholder, context.getString(R.string.error))
//        widget.setViewVisibility(R.id.placeholder, VISIBLE)
//      } else {
//        // Hide Loading, show status bar
//        widget.setViewVisibility(R.id.placeholder, GONE)
//
//        // Render the chart
//        widget.setImageViewBitmap(R.id.canvas, createRenderedBitmap(result))
//      }
//
//      Log.d(BoueeWidgetProvider::class.toString(), "Updating widget.")
//      appWidgetManager.updateAppWidget(widgetId, widget)
    }

    override fun doInBackground(vararg params: Int?): ForecastResponse {
      try {
        val spotId = params[0] ?: 384

        //    val objectMapper = jacksonObjectMapper()
        //    objectMapper.registerModule(JavaTimeModule())
        //    val forecastJson: String = context.assets.open("forecast_response.json")?.bufferedReader()
        //        ?.readText().orEmpty()
        //
        //
        //    // Deserialize
        //    return objectMapper.readValue(forecastJson)

        // Get the forecast config
        val forecastConfig = ForecastConfig(apiKey = context.getString(R.string.msw_key))
        val forecastService = ForecastService(forecastConfig)

        // Get the forecast
        return forecastService.getForecasts(spotId)
      } catch (e: Exception) {
        this.exception = e
        return emptyArray()
      }
    }
  }
}