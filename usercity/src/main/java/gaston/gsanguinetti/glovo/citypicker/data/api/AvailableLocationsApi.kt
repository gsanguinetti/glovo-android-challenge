package gaston.gsanguinetti.glovo.citypicker.data.api

import gaston.gsanguinetti.glovo.citypicker.data.model.City
import gaston.gsanguinetti.glovo.citypicker.data.model.Country
import io.reactivex.Single
import retrofit2.http.GET

interface AvailableLocationsApi {

    @GET("api/cities/")
    fun getCities() :Single<List<City>>

    @GET("api/countries/")
    fun getCountries() :Single<List<Country>>
}