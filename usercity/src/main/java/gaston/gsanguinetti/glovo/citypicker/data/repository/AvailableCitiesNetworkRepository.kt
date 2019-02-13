package gaston.gsanguinetti.glovo.citypicker.data.repository

import gaston.gsanguinetti.glovo.citypicker.data.api.AvailableLocationsApi
import gaston.gsanguinetti.glovo.citypicker.data.mapper.AvailableCityMapper
import gaston.gsanguinetti.glovo.citypicker.data.model.AvailableCitiesCacheData
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCity
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCitiesRepository
import gaston.gsanguinetti.glovo.network.datasource.api.NetworkApi
import io.reactivex.Single

class AvailableCitiesNetworkRepository(
    private val availableCityMapper: AvailableCityMapper,
    private val networkApi: NetworkApi
) : AvailableCitiesRepository {

    override fun getAvailableCities(): Single<List<AvailableCity>> =
        networkApi.makeCacheableApiCallForResponse(
            AvailableLocationsApi::class.java,
            AvailableCitiesCacheData::class.java,
            AvailableCitiesCacheData::class.java.simpleName,
            { AvailableCitiesCacheData(it) },
            { it?.cities },
            { it.getCities() })
            .map { it.map { city -> availableCityMapper.mapFromEntity(city) } }
}