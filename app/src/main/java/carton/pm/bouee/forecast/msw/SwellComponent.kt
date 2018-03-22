package carton.pm.bouee.forecast.msw

data class SwellComponent(
    val height: Float,
    val period: Int,
    val direction: Float,
    val compassDirection: String
)