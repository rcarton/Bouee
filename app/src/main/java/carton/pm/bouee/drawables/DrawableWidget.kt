package carton.pm.bouee.drawables

import android.graphics.*
import android.graphics.drawable.Drawable
import carton.pm.bouee.forecast.getLocalDayOfForecast
import carton.pm.bouee.forecast.getNumberOfDaysInForecast
import carton.pm.bouee.forecast.msw.Forecast
import java.lang.Float.min
import java.time.Instant
import java.time.LocalDate

// STYLE
const val PADDING_BETWEEN_DAYS = 0f
const val STATUS_BAR_HEIGHT = 38
const val MAX_WAVE_HEIGHT_FT = 10f

class DrawableWidget(private val forecasts: Array<Forecast>): Drawable() {

  private val barPaint = Paint()

  init {
    barPaint.isAntiAlias = true
    barPaint.style = Paint.Style.FILL
    barPaint.color = Color.parseColor("#3cbbe8")
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

  override fun draw(canvas: Canvas) {
    if (forecasts.isEmpty()) {
      return
    }

    // Will be used to display previous forecasts differently
    val now = Instant.now()

    // Save
    val height = canvas.height.toFloat() - STATUS_BAR_HEIGHT - 5
    val width = canvas.width.toFloat()

    val numberOfDaysInForecast = getNumberOfDaysInForecast(forecasts)

    // Draw the status bar
    val statusBarPaint = Paint()
    statusBarPaint.isAntiAlias = true
    statusBarPaint.color = Color.parseColor("#88000000")
    statusBarPaint.textSize = STATUS_BAR_HEIGHT.toFloat()
    canvas.drawText("Far Rockaway ${Instant.now()}", 0f, canvas.height.toFloat() - 5 , statusBarPaint)

    // Draw the grid
    val grid = Grid(width, height, MAX_WAVE_HEIGHT_FT)
    grid.draw(canvas)

    // Figure out the width of a bar
    val barWidth = (width - (numberOfDaysInForecast - 1) * PADDING_BETWEEN_DAYS) / forecasts.size
    val barHeight = height

    val dayPadding = width * PADDING_BETWEEN_DAYS

    var lastDay: LocalDate = getLocalDayOfForecast(forecasts[0])
    var posLeft = 0f
    forecasts.forEachIndexed { i, forecast ->

      // If the days changed, add the day padding
      val currentDay = getLocalDayOfForecast(forecast)
      if (lastDay != currentDay) {
        posLeft += dayPadding
        lastDay = currentDay
        grid.drawDayLine(canvas, posLeft)
      }

      val swellHeight = forecast.swell.components.combined.height
      val waveBarHeight = min(swellHeight, MAX_WAVE_HEIGHT_FT)/MAX_WAVE_HEIGHT_FT * barHeight
      println("barIndex=$i posLeft=$posLeft swellHeight=$swellHeight waveHeight=$waveBarHeight barWidth=$barWidth")


      // TODO: different color for the weekend?
      println(RectF(posLeft, height-waveBarHeight, posLeft+barWidth, height))
      canvas.drawRect(RectF(posLeft, height-waveBarHeight, posLeft+barWidth+1f, height), barPaint)
      posLeft += barWidth
    }
  }
}