package de.iomu.reddit_experiment.data.model

data class Thing2<out T>(val data: T, val name: String, val id: String, val kind: String)

data class Listing<out T>(val children: List<T>, val before: String?, val after: String?)