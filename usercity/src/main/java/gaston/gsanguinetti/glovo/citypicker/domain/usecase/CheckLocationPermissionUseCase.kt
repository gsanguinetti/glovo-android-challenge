package gaston.gsanguinetti.glovo.citypicker.domain.usecase

import gaston.gsanguinetti.glovo.base.domain.SingleUseCase
import gaston.gsanguinetti.glovo.citypicker.domain.repository.CheckLocationPermissionRepository
import io.reactivex.Single

class CheckLocationPermissionUseCase(
    private val checkLocationPermissionRepository: CheckLocationPermissionRepository
) : SingleUseCase<Boolean, Unit>() {

    public override fun buildUseCaseObservable(params: Unit?): Single<Boolean> =
        checkLocationPermissionRepository.hasLocationPermissionGranted()
            .setUpForUseCase()
}