package carton.pm.bouee.drawables

import android.graphics.*
import android.graphics.drawable.Drawable
import carton.pm.bouee.forecast.getLocalDayOfForecast
import carton.pm.bouee.forecast.getNumberOfDaysInForecast
import carton.pm.bouee.forecast.msw.Forecast
import java.lang.Float.min
import java.time.LocalDate

// STYLE
const val PADDING_BETWEEN_DAYS = .005f
const val MAX_WAVE_HEIGHT_FT = 10f

class DrawableWidget(private val forecasts: Array<Forecast>): Drawable() {

  private val barPaint = Paint()

  init {
    barPaint.isAntiAlias = true
    barPaint.style = Paint.Style.FILL
    barPaint.color = Color.CYAN
  }
  override fun getOpacity(): Int {
    TODO("not implemented")
  }

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented")
  }

  override fun setAlpha(alpha: Int) {
    TODO("not implemented")
  }

  override fun draw(canvas: Canvas?) {
    canvas!!

    if (forecasts.isEmpty()) {
      return
    }

    val height = canvas.height
    val width = canvas.width

    val numberOfDaysInForecast = getNumberOfDaysInForecast(forecasts)

    // Figure out the width of a bar
    val barWidth = ((width - (numberOfDaysInForecast - 1) * PADDING_BETWEEN_DAYS) / forecasts.size).toInt()
    val barHeight = height

    val dayPadding = (width * PADDING_BETWEEN_DAYS).toInt()

    var lastDay: LocalDate = getLocalDayOfForecast(forecasts[0])
    var posLeft = 0
    forecasts.forEachIndexed { i, forecast ->

      // If the days changed, add the day padding
      val currentDay = getLocalDayOfForecast(forecast)
      if (lastDay != currentDay) {
        posLeft += dayPadding
        lastDay = currentDay
      }

      val swellHeight = forecast.swell.components.combined.height
      val waveBarHeight = (min(swellHeight, MAX_WAVE_HEIGHT_FT)/MAX_WAVE_HEIGHT_FT * barHeight).toInt()
      println("barIndex=$i posLeft=$posLeft swellHeight=$swellHeight waveHeight=$waveBarHeight barWidth=$barWidth")

      println(Rect(posLeft, height-waveBarHeight, posLeft+barWidth, height))
      canvas.drawRect(Rect(posLeft, height-waveBarHeight, posLeft+barWidth, height), barPaint)
      posLeft += barWidth
    }
  }


}