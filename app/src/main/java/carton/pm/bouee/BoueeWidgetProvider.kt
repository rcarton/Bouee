package carton.pm.bouee

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RemoteViews
import carton.pm.bouee.drawables.DrawableWidget
import carton.pm.bouee.forecast.ForecastIntentService
import carton.pm.bouee.forecast.msw.ForecastConfig
import carton.pm.bouee.forecast.msw.ForecastResponse
import carton.pm.bouee.forecast.msw.ForecastService


class BoueeWidgetProvider() : AppWidgetProvider() {

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                        appWidgetIds: IntArray) {
    super.onUpdate(context, appWidgetManager, appWidgetIds)
    Log.d(BoueeWidgetProvider::class.toString(), "onUpdate")

    println("Widgets found=${appWidgetIds.size}")

    // For every widget
    appWidgetIds.forEach { widgetId ->
      println("Widget id=$widgetId")


      val handler = object: Handler() {
        override fun handleMessage(msg: Message) {
          val reply = msg.data.getString("forecast")

          Log.d(BoueeWidgetProvider::class.toString(), "Handler received data $reply")
        }
      }

      val intent = Intent(context, ForecastIntentService::class.java)
      intent.putExtra("messenger", Messenger(handler))

      // Start the IntentService
      context.startService(intent)

      // Retrieve the forecast and render
//      RetrieveForecast(widgetId, context, appWidgetManager).execute(384)
    }
  }


  private fun createRenderedBitmap(forecasts: ForecastResponse): Bitmap {
    Log.d(BoueeWidgetProvider::class.toString(), "Creating forecast bitmap")
    val bitmap = Bitmap.createBitmap(1080, 1080/5, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val drawable = DrawableWidget(forecasts)
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
      super.onPostExecute(result)

      // Handle errors
      if (this.exception != null) {
        Log.e(BoueeWidgetProvider::class.toString(), this.exception.toString())
        widget.setTextViewText(R.id.placeholder, context.getString(R.string.error))
        widget.setViewVisibility(R.id.placeholder, VISIBLE)
      } else {
        // Hide Loading, show status bar
        widget.setViewVisibility(R.id.placeholder, GONE)

        // Render the chart
        widget.setImageViewBitmap(R.id.canvas, createRenderedBitmap(result))
      }

      Log.d(BoueeWidgetProvider::class.toString(), "Updating widget.")
      appWidgetManager.updateAppWidget(widgetId, widget)
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