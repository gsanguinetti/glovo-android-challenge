package gaston.gsanguinetti.glovo.citypicker.data.model

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("code") val code: String,
    @SerializedName("country_code") val countryCode: String,
    @SerializedName("name") val name: String,
    @SerializedName("working_area") val workingArea: List<String>
)