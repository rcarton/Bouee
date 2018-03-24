package carton.pm.bouee.drawables

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable

class Grid(width: Float, height: Float, maxWaveHeight: Float): Drawable() {

  val width = width
  val height = height
  val maxWaveHeight = maxWaveHeight

  val gridPaint = Paint()
  init {
    gridPaint.style = Paint.Style.STROKE
    gridPaint.color = Color.parseColor("#88424242")
  }

  override fun draw(canvas: Canvas) {
    var posLeft = 0f

    val sizes = intArrayOf(0, 2, 4, 6, 8)
    for (size in sizes) {
      // Y starts at 0 at the top
      val y = height - (size/maxWaveHeight*height)
      canvas.drawLine(0f, y, width, y, gridPaint)
    }
  }

  fun drawDayLine(canvas: Canvas, x: Float) {
    canvas.drawLine(x, 0f, x, height, gridPaint)
  }

  override fun setAlpha(alpha: Int) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getOpacity(): Int {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun setColorFilter(colorFilter: ColorFilter?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}