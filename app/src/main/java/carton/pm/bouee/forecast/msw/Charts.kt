package carton.pm.bouee.forecast.msw

import java.net.URL

data class Charts(
    val swell: URL,
    val period: URL,
    val wind: URL,
    val pressure: URL,
    val sst: URL?
)