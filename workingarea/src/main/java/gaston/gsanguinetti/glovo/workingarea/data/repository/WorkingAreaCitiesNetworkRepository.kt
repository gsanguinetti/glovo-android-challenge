package gaston.gsanguinetti.glovo.workingarea.data.repository

import gaston.gsanguinetti.glovo.network.datasource.api.NetworkApi
import gaston.gsanguinetti.glovo.workingarea.data.api.AvailableCitiesApi
import gaston.gsanguinetti.glovo.workingarea.data.mapper.CityAreaMapper
import gaston.gsanguinetti.glovo.workingarea.data.model.CityAreaListCacheData
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCity
import gaston.gsanguinetti.glovo.workingarea.domain.repository.WorkingAreaCitiesRepository
import io.reactivex.Single

class WorkingAreaCitiesNetworkRepository(
    private val networkApi: NetworkApi,
    private val cityAreaMapper: CityAreaMapper
) : WorkingAreaCitiesRepository {

    override fun getAvailableCities(): Single<List<AvailableCity>> =
        networkApi.makeCacheableApiCallForResponse(
            AvailableCitiesApi::class.java,
            CityAreaListCacheData::class.java,
            CityAreaListCacheData::class.java.simpleName,
            { CityAreaListCacheData(it) },
            { it?.cityAreaList },
            { it.getCities() })
            .map { it.map { cityAreaMapper.mapFromEntity(it) } }
}