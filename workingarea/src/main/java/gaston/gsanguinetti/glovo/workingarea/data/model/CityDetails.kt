package gaston.gsanguinetti.glovo.workingarea.data.model

import com.google.gson.annotations.SerializedName

data class CityDetails(
    @SerializedName("busy") val busy: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("country_code") val countryCode: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("enabled") val enabled: Boolean,
    @SerializedName("language_code") val languageCode: String,
    @SerializedName("name") val name: String,
    @SerializedName("time_zone") val timeZone: String
)