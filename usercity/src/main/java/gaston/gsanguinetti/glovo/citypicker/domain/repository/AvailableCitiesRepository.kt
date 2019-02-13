package gaston.gsanguinetti.glovo.citypicker.domain.repository

import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCity
import io.reactivex.Single

interface AvailableCitiesRepository {
    fun getAvailableCities(): Single<List<AvailableCity>>
}