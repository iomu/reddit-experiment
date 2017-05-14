package de.iomu.reddit_experiment.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import javax.annotation.Nullable;

@AutoValue
public abstract class CommentModel implements Votable, Created {
    public abstract String author();
    public abstract String body();
    public abstract int score();
    @Json(name = "score_hidden")  public abstract boolean isScoreHidden();
    @Nullable public abstract Thing<Listing<Thing<CommentItem>>> replies();

    /*public static Comment create(int ups, int downs, long created, long createdUTC,
                                 String author, String body, int score, boolean scoreHidden, List<Comment> replies) {
        return new AutoValue_Comment(ups, downs, created, createdUTC, author, body, score, scoreHidden, replies);
    }*/

    public static JsonAdapter<CommentModel> jsonAdapter(Moshi moshi) {
        return new AutoValue_CommentModel.MoshiJsonAdapter(moshi);
    }

    public static TypeAdapter<CommentModel> typeAdapter(Gson gson) {
        return new AutoValue_CommentModel.GsonTypeAdapter(gson);
    }
}
