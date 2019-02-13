package gaston.gsanguinetti.glovo.citypicker.presentation.model

import android.view.View
import androidx.core.content.ContextCompat
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import gaston.gsanguinetti.glovo.citypicker.R
import kotlinx.android.synthetic.main.activity_city_picker_country_item.view.*

data class CountryItem(
    val name: String
) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.countryTextView.text = name
        setCollapsingItemStatus(viewHolder.itemView)
        viewHolder.itemView.setOnClickListener {
            expandableGroup.onToggleExpanded()
            setCollapsingItemStatus(viewHolder.itemView)
        }
    }

    private fun setCollapsingItemStatus(itemView: View) {
        itemView.countryListStatusImageView.setImageResource(
            if (expandableGroup.isExpanded) R.drawable.ic_keyboard_arrow_up
            else R.drawable.ic_keyboard_arrow_down
        )
        itemView.countryTextView.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                if (expandableGroup.isExpanded) R.color.countryPicked
                else R.color.itemBlack
            )
        )
        itemView.iconImageView.setColorFilter(
            ContextCompat.getColor(
                itemView.context,
                if (expandableGroup.isExpanded) R.color.countryPicked else R.color.itemBlack
            ),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    override fun getLayout(): Int = R.layout.activity_city_picker_country_item

}