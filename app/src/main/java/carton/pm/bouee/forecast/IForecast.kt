package carton.pm.bouee.forecast

interface IForecast {
  val swell: Swell
  val rating: Int
  val timestamp: Long
}