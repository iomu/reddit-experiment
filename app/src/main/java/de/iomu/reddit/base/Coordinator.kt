package de.iomu.reddit.base

import io.reactivex.Observable

interface Coordinator<in V : MviView<*, *>> {
    fun attachView(view: V)
    fun detachView(retainInstance: Boolean)
}

interface MviView<I, in A> {
    val intentions: Observable<I>
    fun apply(action: A)
}

interface StateRenderer<in S> {
    fun render(state: S)
}