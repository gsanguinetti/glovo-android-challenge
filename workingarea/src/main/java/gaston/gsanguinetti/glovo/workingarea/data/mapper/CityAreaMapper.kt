package gaston.gsanguinetti.glovo.workingarea.data.mapper

import gaston.gsanguinetti.glovo.base.data.DataMapper
import gaston.gsanguinetti.glovo.workingarea.data.model.CityArea
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCity

class CityAreaMapper : DataMapper<CityArea, AvailableCity> {

    override fun mapFromEntity(entity: CityArea): AvailableCity =
        AvailableCity(entity.code, entity.workingArea)

    override fun mapToEntity(domainModel: AvailableCity): CityArea {
        throw NotImplementedError()
    }

}