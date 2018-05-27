package carton.pm.bouee.drawables

interface Unit {
  val multiplier: Float
}

enum class LengthUnit(override val multiplier: Float): Unit {
  FOOT(3.28084f),
  METER(1f),
}

enum class SpeedUnit(override val multiplier: Float): Unit {
  KNOT(1.94384f),
  KILOMETERS_PER_HOUR(3.5999916767997f),
  METERS_PER_SECOND(1f),
  MILES_PER_HOUR(2.2369311202576875885f),
}

fun convert(value: Float, from: Unit, to: Unit): Float {
  if (from::class != to::class) {
    throw IllegalArgumentException("Cannot convert from ${from::class} to ${to::class}.")
  }
  return value / from.multiplier * to.multiplier
}