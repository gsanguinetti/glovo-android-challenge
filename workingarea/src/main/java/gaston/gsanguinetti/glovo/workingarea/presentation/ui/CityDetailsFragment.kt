package gaston.gsanguinetti.glovo.workingarea.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gaston.gsanguinetti.glovo.base.presentation.nonNullObserve
import gaston.gsanguinetti.glovo.workingarea.R
import gaston.gsanguinetti.glovo.workingarea.presentation.model.CityDetailsUiState
import gaston.gsanguinetti.glovo.workingarea.presentation.viewmodel.WorkingAreaViewModel
import kotlinx.android.synthetic.main.fragment_city_details.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CityDetailsFragment : Fragment() {

    private val workingAreaViewModel: WorkingAreaViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_city_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun bindViewModel() = workingAreaViewModel.run {
        cityDetailsUiState.nonNullObserve(this@CityDetailsFragment) {
            when (it) {
                is CityDetailsUiState.NotAvailable -> {
                    cityNameTextView.isEnabled = false
                    cityNameTextView.text = getString(R.string.city_not_available)

                    currencyItemView.isEnabled = false
                    isEnabledItemView.isEnabled = false
                    languageCodeView.isEnabled = false
                    timeZoneItemView.isEnabled = false
                }
                is CityDetailsUiState.Data -> {
                    cityNameTextView.isEnabled = true
                    cityNameTextView.text = it.cityDetailsContent.name

                    it.cityDetailsContent.run {
                        currencyItemView.setDetailItemValue(currency)
                        isEnabledItemView.setDetailItemValue(getString(if (enabled) R.string.yes else R.string.no))
                        languageCodeView.setDetailItemValue(languageCode)
                        timeZoneItemView.setDetailItemValue(timeZone)
                    }
                }
            }
        }
    }

    private fun CityDetailItemView.setDetailItemValue(value :String) {
        isEnabled = true
        setValue(value)
    }
}