package de.iomu.reddit_experiment.data.model

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.squareup.moshi.*
import java.lang.reflect.ParameterizedType

data class CommentResponse(val link: Link, val comments: Listing<CommentItem>)

sealed class CommentItem {
    data class Comment(val comment: CommentModel) : CommentItem()
    data class More(val moreComments: MoreComments) : CommentItem()
}

/*class CommentResponseAdapter(val moshi: Moshi) : JsonAdapter<CommentResponse>() {
    private val linkAdapter: JsonAdapter<Thing<Listing<Thing<Link>>>> =
            moshi.adapter(Types.newParameterizedType(Thing::class.java,
                    Types.newParameterizedType(Listing::class.java,
                            Types.newParameterizedType(Thing::class.java, Link::class.java))))
    private val commentsAdapter: JsonAdapter<Thing<Listing<Thing<CommentModel>>>> =
            moshi.adapter(Types.newParameterizedType(Thing::class.java,
                    Types.newParameterizedType(Listing::class.java,
                            Types.newParameterizedType(Thing::class.java, CommentModel::class.java))))
    override fun toJson(writer: JsonWriter, value: CommentResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fromJson(reader: JsonReader): CommentResponse {
        reader.beginArray()
        val link = linkAdapter.fromJson(reader)
        if (!reader.hasNext()) throw IllegalStateException("Unexpected end of commentresponse")
        val comments = commentsAdapter.fromJson(reader)
        reader.endArray()
        val commentListing = Listing(comments.data().children.map { it.data() }, comments.data().before, comments.data().after)
        return CommentResponse(link.data().children.first().data(), commentListing)
    }

}*/

