package gaston.gsanguinetti.glovo.workingarea.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import gaston.gsanguinetti.glovo.base.presentation.hide
import gaston.gsanguinetti.glovo.base.presentation.nonNullObserve
import gaston.gsanguinetti.glovo.base.presentation.show
import gaston.gsanguinetti.glovo.workingarea.R
import gaston.gsanguinetti.glovo.workingarea.presentation.model.CityDetailsUiState
import gaston.gsanguinetti.glovo.workingarea.presentation.model.WorkingAreaUiState
import gaston.gsanguinetti.glovo.workingarea.presentation.viewmodel.WorkingAreaViewModel
import kotlinx.android.synthetic.main.activity_working_area.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class WorkingAreaActivity : AppCompatActivity() {

    private val workingAreaViewModel: WorkingAreaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        check(!intent.getStringExtra(CITY_CODE_EXTRA).isNullOrEmpty())
        setContentView(R.layout.activity_working_area)
        startViewModel()
        disableBottomLayout()
        bindViewModel()
    }

    private fun startViewModel() {
        workingAreaViewModel.startWorkingArea((intent.getStringExtra(WorkingAreaActivity.CITY_CODE_EXTRA)))
    }

    private fun bindViewModel() = workingAreaViewModel.run {

        mapContentRetryLoadingButton.setOnClickListener { startViewModel() }

        cityDetailsUiState.nonNullObserve(this@WorkingAreaActivity) {
            when (it) {
                is CityDetailsUiState.NotAvailable -> {
                    enableBottomLayout()
                    setCollapseBottomLayoutIfHidden()
                }
                is CityDetailsUiState.Error -> {
                    disableBottomLayout()
                    makeErrorSnackBar()
                }
                is CityDetailsUiState.Data -> {
                    enableBottomLayout()
                    expandBottomLayoutIfHidden()
                    cityDetailsBottomSheet.show()
                }
            }
        }

        workingAreaUiState.nonNullObserve(this@WorkingAreaActivity) {
            when (it) {
                is WorkingAreaUiState.Loading -> {
                    mapContentLoadingProgressBar.show()
                    errorContainerView.hide()
                    mapCoordinatorLayout.hide()
                }
                is WorkingAreaUiState.Error -> {
                    mapContentLoadingProgressBar.hide()
                    errorContainerView.show()
                    mapCoordinatorLayout.hide()
                }
                is WorkingAreaUiState.Data -> {
                    mapContentLoadingProgressBar.hide()
                    errorContainerView.hide()
                    mapCoordinatorLayout.show()
                }
            }
        }
    }

    private fun enableBottomLayout() {
        BottomSheetBehavior.from(cityDetailsBottomSheet).run {
            isHideable = false
        }
    }

    private fun expandBottomLayoutIfHidden() {
        BottomSheetBehavior.from(cityDetailsBottomSheet).run {
            if (state == BottomSheetBehavior.STATE_HIDDEN) state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setCollapseBottomLayoutIfHidden() {
        BottomSheetBehavior.from(cityDetailsBottomSheet).run {
            if (state == BottomSheetBehavior.STATE_HIDDEN) state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun disableBottomLayout() {
        BottomSheetBehavior.from(cityDetailsBottomSheet).run {
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun makeErrorSnackBar() {
        Snackbar.make(containerView, R.string.loading_map_content_error, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { workingAreaViewModel.onRetryMapMovedRequest() }
            .show()
    }

    companion object {
        private const val CITY_CODE_EXTRA = "cityCodeExtra"
        private const val WORKING_AREA_REQUEST_CODE = 400

        fun isWorkingAreaRequestCode(requestCode: Int) =
            requestCode == WORKING_AREA_REQUEST_CODE

        fun startActivityForResult(activity: AppCompatActivity, currentCityCode: String) {
            activity.startActivityForResult(
                Intent(activity, WorkingAreaActivity::class.java)
                    .apply { putExtra(CITY_CODE_EXTRA, currentCityCode) },
                WORKING_AREA_REQUEST_CODE
            )
        }
    }
}
