package carton.pm.bouee.drawables

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import java.time.DayOfWeek

class Days(textHeight: Float) {

  private val paint = Paint()
  init {
    paint.style = Paint.Style.FILL
    paint.isAntiAlias = true
    paint.color = Color.parseColor("#60424242")
    paint.textSize = textHeight
    paint.typeface = Typeface.DEFAULT_BOLD
  }

  fun drawDay(canvas: Canvas, x: Float, y: Float, dayOfWeek: DayOfWeek) {
    canvas.drawText("${dayOfWeek.toString().substring(0, 3).toUpperCase()}", x, y, paint)
  }
}