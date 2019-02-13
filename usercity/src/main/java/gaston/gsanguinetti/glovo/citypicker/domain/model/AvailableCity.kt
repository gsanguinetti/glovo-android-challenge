package gaston.gsanguinetti.glovo.citypicker.domain.model

data class AvailableCity (
    val code: String,
    val workingArea: List<String>,
    val countryCode: String,
    val name: String
)