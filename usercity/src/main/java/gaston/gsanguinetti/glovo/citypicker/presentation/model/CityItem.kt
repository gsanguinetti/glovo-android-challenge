package gaston.gsanguinetti.glovo.citypicker.presentation.model

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gaston.gsanguinetti.glovo.citypicker.R
import kotlinx.android.synthetic.main.activity_city_picker_city_item.view.*

data class CityItem(
    val code: String,
    val label: String
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.cityTextView.text = label
    }

    override fun getLayout(): Int = R.layout.activity_city_picker_city_item
}