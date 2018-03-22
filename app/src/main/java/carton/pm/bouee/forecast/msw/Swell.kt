package carton.pm.bouee.forecast.msw

data class Swell(
    val minBreakingHeight: Float,
    val absMinBreakingHeight: Float,
    val maxBreakingHeight: Float,
    val absMaxBreakingHeight: Float,
    val unit: String,
    val components: SwellComponents,
    var probability: Int
)