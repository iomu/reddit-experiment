package de.iomu.reddit.base.mvi

import com.jakewharton.rxrelay2.BehaviorRelay
import de.iomu.reddit.features.subreddit.SubredditContract
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

interface StateRenderer<in S> {
    fun render(state: S)
}

abstract class RxRenderer<S, A, V : MviView<*, A>> : StateRenderer<S> {
    private val stateStream = BehaviorRelay.create<S>()
    private val disposables = CompositeDisposable()

    override fun render(state: S) {
        stateStream.accept(state)
    }

    fun attachView(view: V) {
        val viewActions = viewActions(stateStream)
        disposables.add(viewActions.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.apply(it)
                })
    }

    fun detachView() {
        disposables.clear()
    }

    abstract fun viewActions(stateStream: Observable<S>): Observable<A>
}