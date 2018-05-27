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
const val STATUS_BAR_HEIGHT = 38
const val MAX_WAVE_HEIGHT_FT = 10f

const val TAG = "DrawableWidget"

class DrawableWidget(private val forecasts: Array<Forecast>,
                     private val config: WidgetConfig) : Drawable() {

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

    // Save
    val gridHeight = config.height.toFloat() - STATUS_BAR_HEIGHT - 5
    val gridWidth = config.width.toFloat()

    val numberOfDaysInForecast = getNumberOfDaysInForecast(forecasts)

    // Draw the status bar
    val statusBarPaint = Paint()
    statusBarPaint.isAntiAlias = true
    statusBarPaint.color = Color.parseColor("#88000000")
    statusBarPaint.textSize = STATUS_BAR_HEIGHT.toFloat()
    canvas.drawText("${config.spotName} ${Instant.now()}", 0f, config.height.toFloat() - 5 , statusBarPaint)

    // Draw the grid
    val grid = Grid(gridWidth, gridHeight, config.swellMaxHeightFeet)
    grid.draw(canvas)

    // Days
    val daysHeight = grid.getHeightBetweenLines() * .8f
    val days = Days(daysHeight)

    // Figure out the width of a bar
    val barWidth = gridWidth / forecasts.size
    val barHeight = gridHeight

    var lastDay: LocalDate = getLocalDayOfForecast(forecasts[0])
    var posLeft = 0f

    forecasts.forEachIndexed { i, forecast ->
      val currentDay = getLocalDayOfForecast(forecast)

      val dayChanged = lastDay != currentDay

      // If the day changed, add the day padding and draw a line
      if (dayChanged) {
        lastDay = currentDay
        grid.drawDayLine(canvas, posLeft)
      }

      if (i == 0 || dayChanged) {
        // Draw the 3 letter day
        if (config.showDays) days.drawDay(canvas, posLeft + 2, daysHeight - 2, currentDay.dayOfWeek)
      }


      val swellHeight = forecast.swell.components.combined.height
      val waveBarHeight = min(swellHeight, config.swellMaxHeightFeet)/config.swellMaxHeightFeet * barHeight

//      println("barIndex=$i posLeft=$posLeft swellHeight=$swellHeight waveHeight=$waveBarHeight barWidth=$barWidth")


      // TODO: different color for the weekend?
//      println(RectF(posLeft, height-waveBarHeight, posLeft+barWidth, height))
      canvas.drawRect(RectF(posLeft, gridHeight-waveBarHeight, posLeft+barWidth+1f, gridHeight), barPaint)
      posLeft += barWidth
    }
  }
}