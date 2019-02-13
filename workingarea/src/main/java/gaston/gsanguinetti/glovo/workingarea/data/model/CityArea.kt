package gaston.gsanguinetti.glovo.workingarea.data.model

import com.google.gson.annotations.SerializedName

data class CityArea (
    @SerializedName("code") val code: String,
    @SerializedName("working_area") val workingArea: List<String>
)