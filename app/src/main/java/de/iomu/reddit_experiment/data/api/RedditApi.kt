package de.iomu.reddit_experiment.data.api

import de.iomu.reddit_experiment.data.model.CommentResponse
import de.iomu.reddit_experiment.data.model.Link
import de.iomu.reddit_experiment.data.model.Listing
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApi {
    @GET("r/{subreddit}.json")
    fun getLinksForSubreddit(@Path("subreddit") subreddit: String): Observable<Listing<Link>>

    @GET("r/{subreddit}.json")
    fun rawGetLinksForSubreddit(@Path("subreddit") subreddit: String, @Query("after") after: String? = null): Observable<ResponseBody>

    @GET("r/{subreddit}/comments/{linkId}.json")
    fun getComments(@Path("subreddit") subreddit: String, @Path("linkId") link: String): Observable<CommentResponse>

    @GET("comments/{linkId}.json")
    fun rawGetComments(@Path("linkId") link: String, @Query("depth") depth: Int? = null): Observable<ResponseBody>
}