package gaston.gsanguinetti.glovo.workingarea.presentation.mapper

import gaston.gsanguinetti.glovo.base.data.DataMapper
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCityDetails
import gaston.gsanguinetti.glovo.workingarea.presentation.model.CityDetailsContent

class CityDetailsContentMapper :DataMapper<CityDetailsContent, AvailableCityDetails> {

    override fun mapFromEntity(entity: CityDetailsContent): AvailableCityDetails {
        throw NotImplementedError()
    }

    override fun mapToEntity(domainModel: AvailableCityDetails): CityDetailsContent =
            CityDetailsContent(
                domainModel.currency,
                domainModel.enabled,
                domainModel.languageCode,
                domainModel.name,
                domainModel.timeZone
            )
}