package de.iomu.reddit.data.api

import de.iomu.reddit.data.model.CommentResponse
import de.iomu.reddit.data.model.Link
import de.iomu.reddit.data.model.Listing
import de.iomu.reddit.data.model.Thing
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApi {
    @GET("r/{subreddit}.json")
    fun getLinksForSubreddit(@Path("subreddit") subreddit: String): Observable<Listing<Link>>

    @GET("r/{subreddit}.json")
    fun rawGetLinksForSubreddit(@Path("subreddit") subreddit: String, @Query("after") after: String?): Observable<ResponseBody>

    @GET("r/{subreddit}/comments/{link}.json")
    fun getComments(@Path("subreddit") subreddit: String, @Path("link") link: String): Observable<CommentResponse>

    @GET("r/{subreddit}/comments/{link}.json")
    fun rawGetComments(@Path("subreddit") subreddit: String, @Path("link") link: String): Observable<ResponseBody>
}