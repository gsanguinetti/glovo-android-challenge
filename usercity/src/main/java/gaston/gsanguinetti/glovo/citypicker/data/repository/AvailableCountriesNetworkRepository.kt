package gaston.gsanguinetti.glovo.citypicker.data.repository

import gaston.gsanguinetti.glovo.citypicker.data.api.AvailableLocationsApi
import gaston.gsanguinetti.glovo.citypicker.data.mapper.AvailableCountryMapper
import gaston.gsanguinetti.glovo.citypicker.data.model.AvailableCountriesCacheData
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCountry
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCountriesRepository
import gaston.gsanguinetti.glovo.network.datasource.api.NetworkApi
import io.reactivex.Single

class AvailableCountriesNetworkRepository(
    private val availableCountryMapper: AvailableCountryMapper,
    private val networkApi: NetworkApi
) : AvailableCountriesRepository {

    override fun getAvailableCountries(): Single<List<AvailableCountry>> =
        networkApi.makeCacheableApiCallForResponse(
            apiClass = AvailableLocationsApi::class.java,
            cacheDataClass = AvailableCountriesCacheData::class.java,
            index = AvailableCountry::class.java.simpleName,
            transformFromCache = { it?.countries },
            transformToCache = { AvailableCountriesCacheData(it) }) {
            it.getCountries()
        }.map { it.map { country -> availableCountryMapper.mapFromEntity(country) } }
}