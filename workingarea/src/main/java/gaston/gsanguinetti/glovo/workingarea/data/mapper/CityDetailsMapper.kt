package gaston.gsanguinetti.glovo.workingarea.data.mapper

import gaston.gsanguinetti.glovo.base.data.DataMapper
import gaston.gsanguinetti.glovo.workingarea.data.model.CityDetails
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCityDetails

class CityDetailsMapper : DataMapper<CityDetails, AvailableCityDetails> {

    override fun mapFromEntity(entity: CityDetails): AvailableCityDetails =
        AvailableCityDetails(
            entity.busy,
            entity.code,
            entity.countryCode,
            entity.currency,
            entity.enabled,
            entity.languageCode,
            entity.name,
            entity.timeZone
        )

    override fun mapToEntity(domainModel: AvailableCityDetails): CityDetails {
        throw NotImplementedError()
    }

}