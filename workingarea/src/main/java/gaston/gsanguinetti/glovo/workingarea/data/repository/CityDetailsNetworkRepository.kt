package gaston.gsanguinetti.glovo.workingarea.data.repository

import gaston.gsanguinetti.glovo.network.datasource.api.NetworkApi
import gaston.gsanguinetti.glovo.workingarea.data.api.AvailableCitiesApi
import gaston.gsanguinetti.glovo.workingarea.data.mapper.CityDetailsMapper
import gaston.gsanguinetti.glovo.workingarea.data.model.CityDetails
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCityDetails
import gaston.gsanguinetti.glovo.workingarea.domain.repository.CityDetailsRepository
import io.reactivex.Single

class CityDetailsNetworkRepository(
    private val networkApi: NetworkApi,
    private val cityDetailsMapper: CityDetailsMapper
) : CityDetailsRepository {

    override fun getCityDetails(cityCode: String): Single<AvailableCityDetails> =
        networkApi.makeCacheableApiCallForResponse(
            AvailableCitiesApi::class.java,
            CityDetails::class.java,
            cityCode
        ) { it.getCityDetails(cityCode) }
            .map { cityDetailsMapper.mapFromEntity(it) }
}