package gaston.gsanguinetti.glovo.citypicker.domain.model

data class AvailableCountry(
    val code: String,
    val name: String,
    var cities: List<AvailableCity>
)