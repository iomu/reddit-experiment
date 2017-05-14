package de.iomu.reddit_experiment.data.api


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import de.iomu.reddit_experiment.data.account.UserScope
import de.iomu.reddit_experiment.data.model.*
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Module
class ApiModule {
    @UserScope
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .add(RedditAdapterFactory.create())
                .add { type, annotations, moshi ->
                    if (type == CommentResponse::class.java) {
                        CommentResponseAdapter(moshi)
                    } else {
                        null
                    }
                }
                .build()
    }

    @UserScope
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapterFactory(RedditTypeAdapterFactory.create())
                .registerTypeAdapterFactory(CommentResponseAdapterFactory())
                .create()

    }

    @Provides
    @UserScope
    fun provideRetrofit(moshi: Moshi, gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(HttpUrl.parse("https://www.reddit.com/"))
                .client(client)
                .addConverterFactory(ListingThingConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    @Provides
    @UserScope
    fun provideRedditApi(retrofit: Retrofit): RedditApi {
        return retrofit.create(RedditApi::class.java)
    }
}



class ListingThingConverter<T>(val delegate: Converter<ResponseBody, Thing<Listing<Thing<T>>>>) : Converter<ResponseBody, Listing<T>> {
    override fun convert(value: ResponseBody): Listing<T> {
        val res = delegate.convert(value)
        val unwrapped = res.data().children.map { it.data() }
        return Listing(unwrapped, res.data().before, res.data().after)
    }
}

class ListingThingConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit): Converter<ResponseBody, *> {
        if (type is ParameterizedType && type.rawType == Listing::class.java && type.actualTypeArguments.first() == Comment::class.java) {
            val thingType = Types.newParameterizedType(Thing::class.java, type.actualTypeArguments.first())
            val listingType = Types.newParameterizedType(Listing::class.java, thingType)
            val thingListing = Types.newParameterizedType(Thing::class.java, listingType)
            val delegate: Converter<ResponseBody, Thing<Listing<Thing<Comment>>>> = retrofit.nextResponseBodyConverter(this, thingListing, annotations)
            return ListingThingConverter(delegate)
        } else {
            return retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        }

    }
}