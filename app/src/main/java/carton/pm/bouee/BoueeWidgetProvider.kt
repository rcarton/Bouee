package carton.pm.bouee

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.RemoteViews
import carton.pm.bouee.drawables.DrawableWidget
import carton.pm.bouee.forecast.msw.ForecastResponse
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


class BoueeWidgetProvider() : AppWidgetProvider() {

  private val objectMapper = jacksonObjectMapper()

  init {
    println("Init BoueeWidgetProvider")
    objectMapper.registerModule(JavaTimeModule())
  }

  override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?,
                        appWidgetIds: IntArray?) {
    super.onUpdate(context, appWidgetManager, appWidgetIds)

    println("Widgets found=${appWidgetIds?.size ?: 0}")

    // For every widget
    appWidgetIds?.forEach { widgetId ->
      println("Widget id=$widgetId")

      val widget = RemoteViews(context?.packageName, R.layout.bouee)

      // Get the forecast
      val forecastJson: String = context?.assets?.open("forecast_response.json")?.bufferedReader()
          ?.readText().orEmpty()

      // Deserialize
      val forecasts: ForecastResponse = objectMapper.readValue(forecastJson)

      // Get the first forecast
      val forecast = forecasts[0]

//      // Set the day label
//      widget.setTextViewText(R.id.dayview_label, forecast.timestamp.toString())
//
//      // Set the rating
//      widget.setTextViewText(R.id.dayview_rating, forecast.solidRating.toString())

      // Weird ass shitovic
//      widget.addView(R.id.view_container, createDayView(context))
//      widget.addView(R.id.view_container, createDayView(context))

      widget.setImageViewBitmap(R.id.canvas, createRenderedBitmap(context, forecasts))

      println("Updating widgetId=$widgetId with ts=${forecast.timestamp}")
      appWidgetManager?.updateAppWidget(widgetId, widget);
    }
  }

  private fun createRenderedBitmap(context: Context?, forecasts: ForecastResponse): Bitmap {
    val bitmap = Bitmap.createBitmap(1080, 1080/5, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val drawable = DrawableWidget(forecasts)
    drawable.draw(canvas)

    return bitmap
  }

  private fun createDayView(context: Context?): RemoteViews {
    val newView = RemoteViews(context?.packageName, R.layout.dayview_component)
    newView.setTextViewText(R.id.dayview_rating, "777")

    // Abort, I'll make an image view with a canvas. This layout technique is madness.
    // Try drawing I guess: https://stackoverflow.com/questions/9410468/draw-on-canvas-to-home-screen-widget-android?rq=1
    // Try Vector Drawable too for a smooth line and turn it into a bitmap
    return newView
  }
}