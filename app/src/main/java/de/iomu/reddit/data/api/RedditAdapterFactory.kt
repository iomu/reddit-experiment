package de.iomu.reddit.data.api

import com.google.gson.TypeAdapterFactory
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory
import com.ryanharter.auto.value.moshi.MoshiAdapterFactory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

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