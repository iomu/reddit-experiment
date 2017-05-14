package de.iomu.reddit_experiment.base.mvi

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

interface Coordinator<in V : MviView<*, *>> {
    fun attachView(view: V)
    fun detachView(retainInstance: Boolean)
}

interface MviView<I, in A> {
    val intentions: Observable<I>
    fun apply(action: A)
}

abstract class BaseCoordinator<I, A, R, VS, in V: MviView<I, *>> : Coordinator<V> {
    private val disposables = CompositeDisposable()

    private val intentionRelay = PublishRelay.create<I>()
    private var started = false

    fun start() {
        val actions = intentionRelay.map { toAction(it) }
        val results = handleActions(actions)

        val states = results.scan(initialState) { old, result ->
            reduce(old, result)
        }.skip(1)
        states.subscribe { render(it) }
    }

    override fun attachView(view: V) {
        if (!started) {
            start()
            started = true
        }
        disposables.add(view.intentions.subscribe(intentionRelay))
    }

    override fun detachView(retainInstance: Boolean) {
        disposables.clear()
    }

    abstract val initialState: VS
    abstract fun toAction(intention: I): A
    abstract fun handleActions(actions: Observable<A>): Observable<R>
    abstract fun reduce(state: VS, result: R): VS
    abstract fun render(state: VS)
}