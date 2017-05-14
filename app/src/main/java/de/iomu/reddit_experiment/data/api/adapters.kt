package de.iomu.reddit_experiment.data.api

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import de.iomu.reddit_experiment.data.model.*
import java.lang.reflect.ParameterizedType

class CommentResponseTypeAdapter(val gson: Gson) : TypeAdapter<CommentResponse>() {
    private val linkAdapter: TypeAdapter<Thing<Listing<Thing<Link>>>> = gson.getAdapter(TypeToken.getParameterized(Thing::class.java,
            TypeToken.getParameterized(Listing::class.java,
                    TypeToken.getParameterized(Thing::class.java, Link::class.java).type).type))
            as TypeAdapter<Thing<Listing<Thing<Link>>>>

    private val commentAdapter: TypeAdapter<Thing<Listing<Thing<CommentItem>>>> = gson.getAdapter(TypeToken.getParameterized(Thing::class.java,
            TypeToken.getParameterized(Listing::class.java,
                    TypeToken.getParameterized(Thing::class.java, CommentItem::class.java).type).type))
            as TypeAdapter<Thing<Listing<Thing<CommentItem>>>>

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

class CommentItemAdapter(val gson: Gson) : TypeAdapter<Thing<CommentItem>>() {
    private val commentAdapter: TypeAdapter<CommentModel> = gson.getAdapter(CommentModel::class.java)
    private val moreAdapter: TypeAdapter<MoreComments> = gson.getAdapter(MoreComments::class.java)

    override fun read(reader: com.google.gson.stream.JsonReader): Thing<CommentItem>? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        reader.beginObject()
        var data: CommentItem? = null
        var id = ""
        var name = ""
        var kind = "t1"
        while (reader.hasNext()) {
            val _name = reader.nextName()
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                continue
            }
            when (_name) {
                "data" -> {
                    data = when (kind) {
                        "t1" -> CommentItem.Comment(commentAdapter.read(reader))
                        "more" -> CommentItem.More(moreAdapter.read(reader))

                        else -> throw IllegalStateException("Unexpected kind: $kind")
                    }
                }
                "id" -> {
                    id = reader.nextString()
                }
                "name" -> {
                    name = reader.nextString()
                }
                "kind" -> {
                    kind = reader.nextString()
                }
                else -> {
                    reader.skipValue()
                }
            }
        }
        reader.endObject()
        return Thing.create(data, id, name, kind)
    }

    override fun write(writer: com.google.gson.stream.JsonWriter, value: Thing<CommentItem>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class CommentItemAdapterFactory : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val t = type.type
        if (t is ParameterizedType && t.rawType == Thing::class.java && t.actualTypeArguments.first() == CommentItem::class.java) {
            return CommentItemAdapter(gson) as TypeAdapter<T>
        } else {
            return null
        }
    }
}