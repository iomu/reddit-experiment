package de.iomu.reddit.util

sealed class Option<out T> {
    abstract fun isEmpty(): Boolean

    fun nonEmpty(): Boolean = isDefined()

    fun isDefined(): Boolean = !isEmpty()

    abstract fun get(): T

    fun orNull(): T? = if (isEmpty()) {
        null
    } else {
        get()
    }

    inline fun <R> map(f: (T) -> R): Option<R> = if (isEmpty()) {
        None
    } else {
        Some(f(get()))
    }

    inline fun <P1, R> map(p1: Option<P1>, f: (T, P1) -> R): Option<R> = if (isEmpty()) {
        None
    } else {
        p1.map { pp1 -> f(get(), pp1) }
    }

    inline fun <R> fold(ifEmpty: () -> R, f: (T) -> R): R = if (isEmpty()) {
        ifEmpty()
    } else {
        f(get())
    }

    inline fun <R> flatMap(f: (T) -> Option<R>): Option<R> = if (isEmpty()) {
        None
    } else {
        f(get())
    }

    inline fun filter(predicate: (T) -> Boolean): Option<T> = if (nonEmpty() && predicate(get())) {
        this
    } else {
        None
    }

    inline fun filterNot(predicate: (T) -> Boolean): Option<T> = if (nonEmpty() && !predicate(get())) {
        this
    } else {
        None
    }

    inline fun exists(predicate: (T) -> Boolean): Boolean = nonEmpty() && predicate(get())

    inline fun forEach(f: (T) -> Unit) {
        if (nonEmpty()) f(get())
    }


    fun toList(): List<T> = if (isEmpty()) {
        listOf()
    } else {
        listOf(get())
    }

    infix fun <X> and(value: Option<X>): Option<X> = if (isEmpty()) {
        None
    } else {
        value
    }

    object None : Option<Nothing>() {
        override fun get() = throw NoSuchElementException("None.get")

        override fun isEmpty() = true

        override fun equals(other: Any?): Boolean = when (other) {
            is None -> true
            else -> false
        }

        override fun hashCode(): Int = Integer.MAX_VALUE
    }

    data class Some<out T>(val t: T) : Option<T>() {
        override fun get() = t

        override fun isEmpty() = false

    }
}

fun <T> Option<T>.getOrElse(default: () -> T): T = if (isEmpty()) {
    default()
} else {
    get()
}

fun <T> Option<T>.orElse(alternative: () -> Option<T>): Option<T> = if (isEmpty()) {
    alternative()
} else {
    this
}

fun <T> T?.toOption(): Option<T> = this?.let { Option.Some(it) } ?: Option.None
