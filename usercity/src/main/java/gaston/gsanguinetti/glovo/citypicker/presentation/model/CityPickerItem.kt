package gaston.gsanguinetti.glovo.citypicker.presentation.model

import com.xwray.groupie.ExpandableGroup

data class CityPickerItem(
    val countryItem: CountryItem,
    val cityItems: List<CityItem>
) {
    val section: ExpandableGroup
        get() {
            val expandableGroup = ExpandableGroup(countryItem)
            expandableGroup.addAll(cityItems)
            return expandableGroup
        }
}