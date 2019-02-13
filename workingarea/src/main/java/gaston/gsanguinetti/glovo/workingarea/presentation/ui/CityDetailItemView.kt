package gaston.gsanguinetti.glovo.workingarea.presentation.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import gaston.gsanguinetti.glovo.base.presentation.hide
import gaston.gsanguinetti.glovo.base.presentation.show
import gaston.gsanguinetti.glovo.workingarea.R
import kotlinx.android.synthetic.main.view_city_detail_item.view.*

class CityDetailItemView : FrameLayout {

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.view_city_detail_item, this, true)

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CityDetailItemView,
            0, 0
        )

        try {
            val itemResId = typedArray.getResourceId(R.styleable.CityDetailItemView_icon, 0)
            val value = typedArray.getString(R.styleable.CityDetailItemView_value)

            setIconResId(itemResId)
            value?.let { setValue(it) }

        } finally {
            typedArray.recycle()
        }
    }

    fun setIconResId(resId: Int) = detailIconImageView.setImageResource(resId)

    fun setValue(value: String) {
        detailTextView.text = value
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if(enabled) {
            skeletonView.hide()
            detailTextView.show()
        } else {
            skeletonView.show()
            detailTextView.hide()
        }
    }
}

