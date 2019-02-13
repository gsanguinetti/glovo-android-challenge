package gaston.gsanguinetti.glovo.workingarea.presentation.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import gaston.gsanguinetti.glovo.base.presentation.nonNullObserve
import gaston.gsanguinetti.glovo.workingarea.R
import gaston.gsanguinetti.glovo.workingarea.presentation.model.WorkingAreaUiState
import gaston.gsanguinetti.glovo.workingarea.presentation.viewmodel.WorkingAreaViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WorkingAreaMapFragment : SupportMapFragment(), GoogleMap.OnMarkerClickListener {

    private val workingAreaViewModel: WorkingAreaViewModel by sharedViewModel()
    private var workingMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playServicesConnectionResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)

        if (hasGooglePlayServicesEnabled(playServicesConnectionResult)) {
            getMapAsync {
                workingMap = it
                workingMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.style_json))
                workingMap?.setOnMarkerClickListener(this)
                val collapsingBehavior = CollapseBehavior.from(view)
                collapsingBehavior.onCollapsing = { dy ->
                    val mapBottomSheetPadding = dy.toInt() * -1
                    workingMap?.setPadding(0, mapBottomSheetPadding, 0, mapBottomSheetPadding)
                }
                workingMap?.setOnMapLoadedCallback { bindViewModel(savedInstanceState) }
            }
        } else showGooglePlayServicesErrorNotification(playServicesConnectionResult)
    }

    private fun bindViewModel(savedInstanceState: Bundle?) = workingAreaViewModel.run {
        workingAreaUiState.nonNullObserve(this@WorkingAreaMapFragment) {
            if (it is WorkingAreaUiState.Data) {
                workingMap?.run {
                    setOnCameraIdleListener { refreshMapContentIfShould() }
                }
                it.currentCityBounds?.run {
                    if (savedInstanceState == null) displayMapAt(this)
                    else refreshMapContentIfShould()
                }
            }
        }

        displayCityPolygons.nonNullObserve(this@WorkingAreaMapFragment) {
            clearMap()
            displayPolygons(it)
        }

        displayCityMarkers.nonNullObserve(this@WorkingAreaMapFragment) {
            clearMap()
            displayMarkers(it)
        }

        focusOnBounds.nonNullObserve(this@WorkingAreaMapFragment) { displayMapAt(it) }
    }

    private fun refreshMapContentIfShould() {
        workingMap?.cameraPosition?.run { workingAreaViewModel.onMapMoved(zoom.toInt(), target) }
    }

    private fun displayMapAt(latLngBounds: LatLngBounds) {
        workingMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0))
    }

    private fun clearMap() = workingMap?.clear()

    private fun displayPolygons(polygonOptionsList: List<PolygonOptions>) {
        polygonOptionsList.filter { it.points.isNotEmpty() }.forEach {
            it.fillColor(ContextCompat.getColor(activity!!, R.color.polygonFillColor))
                .strokeColor(Color.TRANSPARENT)
            workingMap?.addPolygon(it)
        }
    }

    private fun displayMarkers(markerOptionsList: List<MarkerOptions>) =
        markerOptionsList.forEach {
            workingMap?.addMarker(it.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
        }

    override fun onMarkerClick(marker: Marker?): Boolean {
        workingAreaViewModel.onCityMarkerClick(marker?.title)
        return true
    }

    private fun hasGooglePlayServicesEnabled(connectionResult: Int): Boolean =
        connectionResult == ConnectionResult.SUCCESS

    private fun showGooglePlayServicesErrorNotification(connectionResult: Int) =
        GoogleApiAvailability.getInstance().showErrorNotification(activity, connectionResult)
}