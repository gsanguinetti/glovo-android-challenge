package gaston.gsanguinetti.glovo.base.domain

import io.reactivex.Maybe
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

abstract class MaybeUseCase<T, in PARAMS> :AbstractDisposableUseCase() {

    protected abstract fun buildUseCaseObservable(params: PARAMS? = null): Maybe<T>

    open fun execute(maybeObserver: DisposableMaybeObserver<T>, params: PARAMS? = null) {
        disposables.clear()

        val observer = this.buildUseCaseObservable(params)
            .setUpForUseCase()
            .subscribeWith(maybeObserver)

        observer.run { addDisposable(this) }
    }

    fun <T> Maybe<T>.setUpForUseCase(): Maybe<T> = subscribeOn(Schedulers.io()).observeOn(Schedulers.computation())
}