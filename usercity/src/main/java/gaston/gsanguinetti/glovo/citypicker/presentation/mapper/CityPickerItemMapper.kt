package gaston.gsanguinetti.glovo.citypicker.presentation.mapper

import gaston.gsanguinetti.glovo.base.data.DataMapper
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCountry
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CityItem
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CityPickerItem
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CountryItem

class CityPickerItemMapper : DataMapper<CityPickerItem, AvailableCountry> {

    override fun mapFromEntity(entity: CityPickerItem): AvailableCountry {
        throw NotImplementedError()
    }

    override fun mapToEntity(domainModel: AvailableCountry): CityPickerItem =
            CityPickerItem(
                CountryItem(domainModel.name),
                domainModel.cities.map { CityItem(it.code, it.name) }
            )
}