package de.iomu.reddit_experiment.data.model

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.*

data class CommentResponse(val link: Link, val comments: Listing<Comment>)

class CommentResponseAdapter(val moshi: Moshi) : JsonAdapter<CommentResponse>() {
    private val linkAdapter: JsonAdapter<Thing<Listing<Thing<Link>>>> =
            moshi.adapter(Types.newParameterizedType(Thing::class.java,
                    Types.newParameterizedType(Listing::class.java,
                            Types.newParameterizedType(Thing::class.java, Link::class.java))))
    private val commentsAdapter: JsonAdapter<Thing<Listing<Thing<Comment>>>> =
            moshi.adapter(Types.newParameterizedType(Thing::class.java,
                    Types.newParameterizedType(Listing::class.java,
                            Types.newParameterizedType(Thing::class.java, Comment::class.java))))
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

}

class CommentResponseTypeAdapter(val gson: Gson) : TypeAdapter<CommentResponse>() {
    private val linkAdapter: TypeAdapter<Thing<Listing<Thing<Link>>>> = gson.getAdapter(TypeToken.getParameterized(Thing::class.java,
            TypeToken.getParameterized(Listing::class.java,
                    TypeToken.getParameterized(Thing::class.java, Link::class.java).type).type))
            as TypeAdapter<Thing<Listing<Thing<Link>>>>

    private val commentAdapter: TypeAdapter<Thing<Listing<Thing<Comment>>>> = gson.getAdapter(TypeToken.getParameterized(Thing::class.java,
            TypeToken.getParameterized(Listing::class.java,
                    TypeToken.getParameterized(Thing::class.java, Comment::class.java).type).type))
            as TypeAdapter<Thing<Listing<Thing<Comment>>>>

    override fun write(out: com.google.gson.stream.JsonWriter?, value: CommentResponse?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(reader: com.google.gson.stream.JsonReader): CommentResponse {
        reader.beginArray()
        val link = linkAdapter.read(reader)
        if (!reader.hasNext()) throw IllegalStateException("Unexpected end of commentresponse")
        val comments = commentAdapter.read(reader)
        reader.endArray()
        val commentListing = Listing(comments.data().children.map { it.data() }, comments.data().before, comments.data().after)
        return CommentResponse(link.data().children.first().data(), commentListing)
    }
}

class CommentResponseAdapterFactory : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType as Class<T>
        if (rawType == CommentResponse::class.java) {
            return CommentResponseTypeAdapter(gson) as TypeAdapter<T>
        } else {
            return null
        }
    }
}