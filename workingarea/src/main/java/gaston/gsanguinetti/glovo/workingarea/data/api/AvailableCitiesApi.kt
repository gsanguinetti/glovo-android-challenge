package gaston.gsanguinetti.glovo.workingarea.data.api

import gaston.gsanguinetti.glovo.workingarea.data.model.CityArea
import gaston.gsanguinetti.glovo.workingarea.data.model.CityDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface AvailableCitiesApi {

    @GET("api/cities/")
    fun getCities() : Single<List<CityArea>>

    @GET("api/cities/{cityCode}")
    fun getCityDetails(@Path("cityCode") cityCode: String) : Single<CityDetails>
}