package carton.pm.bouee.forecast.msw

data class Wind(
    val speed: Int,
    val direction: Float,
    val compassDirection: String,
    val chill: Int,
    val gusts: Int,
    val unit: String
)

