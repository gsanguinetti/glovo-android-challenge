package gaston.gsanguinetti.glovo.citypicker.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.*
import gaston.gsanguinetti.glovo.base.presentation.hide
import gaston.gsanguinetti.glovo.base.presentation.nonNullObserve
import gaston.gsanguinetti.glovo.base.presentation.show
import gaston.gsanguinetti.glovo.citypicker.R
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CityItem
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CityPickerUiState
import gaston.gsanguinetti.glovo.citypicker.presentation.viewmodel.CityPickerViewModel
import kotlinx.android.synthetic.main.activity_city_picker.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CityPickerActivity : BaseLocationFetchActivity(), OnItemClickListener {

    private val cityPickerViewModel: CityPickerViewModel by viewModel()
    private val groupAdapter: GroupAdapter<ViewHolder> by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_picker)

        cityPickerViewModel.run {
            fetchLocationOptions()
            cityPickerRetryLoadingButton.setOnClickListener { fetchLocationOptions() }
            cityPickerItemUiState.nonNullObserve(this@CityPickerActivity) {
                when (it) {
                    is CityPickerUiState.Loading -> {
                        loadingProgressBar.show()
                        errorContainerView.hide()
                        cityPickerRecyclerView.hide()
                    }
                    is CityPickerUiState.Error -> {
                        loadingProgressBar.hide()
                        errorContainerView.show()
                        cityPickerRecyclerView.hide()
                    }
                    is CityPickerUiState.Data -> {
                        loadingProgressBar.hide()
                        cityPickerRecyclerView.show()
                        displayCityList(it.cityPickerItems.map { it.section })
                    }
                }
            }
            cityCodeSelected.nonNullObserve(this@CityPickerActivity) { deliverUserCityResult(it) }
        }
    }

    private fun displayCityList(cityList :List<Group>) {
        cityPickerRecyclerView.layoutManager = LinearLayoutManager(this@CityPickerActivity)
        groupAdapter.setOnItemClickListener(this@CityPickerActivity)
        cityPickerRecyclerView.adapter = groupAdapter
        groupAdapter.addAll(cityList)
    }

    override fun onItemClick(item: Item<*>, view: View) {
        if(item is CityItem) cityPickerViewModel.onCityPicked(item)
    }

    companion object {
        fun startActivityForResult(activity: AppCompatActivity) {
            BaseLocationFetchActivity.startActivityForResult(activity, CityPickerActivity::class.java)
        }
    }
}
