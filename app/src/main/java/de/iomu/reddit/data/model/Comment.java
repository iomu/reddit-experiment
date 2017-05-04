package de.iomu.reddit.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.List;

import javax.annotation.Nullable;

@AutoValue
public abstract class Comment implements Votable, Created {
    public abstract String author();
    public abstract String body();
    public abstract int score();
    @Json(name = "score_hidden")  public abstract boolean isScoreHidden();
    @Nullable public abstract Thing<Listing<Thing<Comment>>> replies();

    /*public static Comment create(int ups, int downs, long created, long createdUTC,
                                 String author, String body, int score, boolean scoreHidden, List<Comment> replies) {
        return new AutoValue_Comment(ups, downs, created, createdUTC, author, body, score, scoreHidden, replies);
    }*/

    public static JsonAdapter<Comment> jsonAdapter(Moshi moshi) {
        return new  AutoValue_Comment.MoshiJsonAdapter(moshi);
    }

    public static TypeAdapter<Comment> typeAdapter(Gson gson) {
        return new AutoValue_Comment.GsonTypeAdapter(gson);
    }
}
