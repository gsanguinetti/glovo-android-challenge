package gaston.gsanguinetti.glovo.workingarea.domain.repository

import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCity
import io.reactivex.Single

interface WorkingAreaCitiesRepository {
    fun getAvailableCities(): Single<List<AvailableCity>>
}