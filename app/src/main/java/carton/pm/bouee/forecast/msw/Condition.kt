package carton.pm.bouee.forecast.msw

data class Condition(
    val pressure: Int,
    val temperature: Int,
    val weather: Int,
    val unitPressure: String,
    val unit: String
)