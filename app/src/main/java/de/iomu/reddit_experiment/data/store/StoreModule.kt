package de.iomu.reddit_experiment.data.store

import com.google.gson.Gson
import com.nytimes.android.external.store2.base.impl.Store
import com.nytimes.android.external.store2.base.impl.StoreBuilder
import com.nytimes.android.external.store2.middleware.GsonParserFactory
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import de.iomu.reddit_experiment.app.RedditApp
import de.iomu.reddit_experiment.data.account.UserScope
import de.iomu.reddit_experiment.data.api.ApiModule
import de.iomu.reddit_experiment.data.api.RedditApi
import de.iomu.reddit_experiment.data.model.CommentResponse
import de.iomu.reddit_experiment.data.model.Link
import de.iomu.reddit_experiment.data.model.Listing
import de.iomu.reddit_experiment.data.model.Thing
import okio.BufferedSource

@Module(includes = arrayOf(ApiModule::class))
class StoreModule {
    @Provides
    @UserScope
    fun provideSubredditStore(gson: Gson, app: RedditApp, redditApi: RedditApi): Store<Listing<Link>, Subreddit> {
        val thingType = Types.newParameterizedType(Thing::class.java, Link::class.java)
        val listingType = Types.newParameterizedType(Listing::class.java, thingType)
        val thingListing = Types.newParameterizedType(Thing::class.java, listingType)

        return StoreBuilder.parsedWithKey<Subreddit, BufferedSource, Listing<Link>>()
                .fetcher {
                    redditApi.rawGetLinksForSubreddit(it.name, it.after).map { it.source() }
                }
                .persister(SourcePersister(app.cacheDir, { it.toString() }))
                .parser(SubredditListingTransformer(GsonParserFactory.createSourceParser(gson, thingListing)))
                .open()

    }

    @Provides
    @UserScope
    fun provideLinkStore(gson: Gson, app: RedditApp, redditApi: RedditApi): Store<CommentResponse, LinkKey> {
        return StoreBuilder.parsedWithKey<LinkKey, BufferedSource, CommentResponse>()
                .fetcher {
                    redditApi.rawGetComments(it.subreddit, it.id).map { it.source() }
                }
                .persister(SourcePersister(app.cacheDir, { it.toString() }))
                .parser(GsonParserFactory.createSourceParser(gson, CommentResponse::class.java))
                .open()

    }
}

data class Subreddit(val name: String, val after: String? = null)
data class LinkKey(val subreddit: String, val id: String)
