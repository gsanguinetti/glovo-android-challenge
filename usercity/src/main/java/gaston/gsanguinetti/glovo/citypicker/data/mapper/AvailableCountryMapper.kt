package gaston.gsanguinetti.glovo.citypicker.data.mapper

import gaston.gsanguinetti.glovo.base.data.DataMapper
import gaston.gsanguinetti.glovo.citypicker.data.model.Country
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCountry

class AvailableCountryMapper : DataMapper<Country, AvailableCountry> {

    override fun mapFromEntity(entity: Country): AvailableCountry =
        AvailableCountry(entity.code, entity.name, listOf())

    override fun mapToEntity(domainModel: AvailableCountry): Country {
        throw NotImplementedError()
    }
}