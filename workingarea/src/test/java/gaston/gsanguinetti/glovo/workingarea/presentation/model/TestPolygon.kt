package gaston.gsanguinetti.glovo.workingarea.presentation.model

import com.google.gson.annotations.SerializedName

data class TestPolygon(
    @SerializedName("coordinates")
    val coordinates: List<List<List<Double>>>
)