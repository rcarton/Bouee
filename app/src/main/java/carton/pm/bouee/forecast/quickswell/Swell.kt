package carton.pm.bouee.forecast.quickswell

data class Swell(
    // Meters
    val significantHeight: Float,
    // Seconds
    val period: Float,
    // Degrees
    val direction: Float
)