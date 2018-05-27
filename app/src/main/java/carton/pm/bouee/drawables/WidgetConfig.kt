package carton.pm.bouee.drawables

import android.graphics.Color
import android.graphics.Paint

/**
 * Use heights and widths relative to the base height and width (ex .05 for 5%).
 */
data class WidgetConfig(

    val spotName: String = "",

    // ---- Options ----
    val showDays: Boolean = true,

    // ---- Dimensions ----
    val width: Int = 1080,
    val height: Int = width/5,

    // ---- Colors ----
    val colorBlue: Int = Color.parseColor("#3cbbe8"),
    val colorDarkGrey: Int = Color.parseColor("#88000000"),

    // ---- Swell ----
    val swellUnit: LengthUnit = LengthUnit.FOOT,
    val swellMaxHeight: Float = 10f, // Maximum swell height in feet
    val swellPaint: Paint = Paint().apply {
      isAntiAlias = true
      style = Paint.Style.FILL
      color = colorBlue
    },

    // ---- Status bar ----
    val statusBarHeightPct: Float = 38f/height,
    val statusBarPaint: Paint = Paint().apply {
      isAntiAlias = true
      color = colorDarkGrey
      textSize = statusBarHeightPct
    }

)