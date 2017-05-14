package de.iomu.reddit_experiment.util

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.minimumDelay(duration: Long, unit: TimeUnit): Observable<T> {
    return zipWith(Observable.interval(duration, unit), BiFunction { a, b -> a })
}