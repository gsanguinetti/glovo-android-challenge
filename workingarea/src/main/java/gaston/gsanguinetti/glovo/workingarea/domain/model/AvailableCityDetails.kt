package gaston.gsanguinetti.glovo.workingarea.domain.model

data class AvailableCityDetails(
    val busy: Boolean,
    val code: String,
    val countryCode: String,
    val currency: String,
    val enabled: Boolean,
    val languageCode: String,
    val name: String,
    val timeZone: String
)