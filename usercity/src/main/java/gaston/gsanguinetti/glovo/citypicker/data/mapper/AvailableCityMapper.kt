package gaston.gsanguinetti.glovo.citypicker.data.mapper

import gaston.gsanguinetti.glovo.base.data.DataMapper
import gaston.gsanguinetti.glovo.citypicker.data.model.City
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCity

class AvailableCityMapper : DataMapper<City, AvailableCity> {

    override fun mapFromEntity(entity: City): AvailableCity =
        AvailableCity(entity.code, entity.workingArea, entity.countryCode, entity.name)

    override fun mapToEntity(domainModel: AvailableCity): City {
        throw NotImplementedError()
    }
}