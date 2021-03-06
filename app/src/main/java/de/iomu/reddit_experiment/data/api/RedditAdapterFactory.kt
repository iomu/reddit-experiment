package de.iomu.reddit_experiment.data.api

import com.google.gson.TypeAdapterFactory
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory
import com.ryanharter.auto.value.moshi.MoshiAdapterFactory
import com.squareup.moshi.JsonAdapter

@MoshiAdapterFactory
abstract class RedditAdapterFactory : JsonAdapter.Factory {
    companion object {
        fun create(): JsonAdapter.Factory {
            return AutoValueMoshi_RedditAdapterFactory()
        }
    }
}

@GsonTypeAdapterFactory
abstract class RedditTypeAdapterFactory : TypeAdapterFactory {
    companion object {
        fun create(): TypeAdapterFactory? {
            return AutoValueGson_RedditTypeAdapterFactory()
        }
    }
}