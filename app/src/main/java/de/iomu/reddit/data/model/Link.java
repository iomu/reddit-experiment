package de.iomu.reddit.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import javax.annotation.Nullable;

@AutoValue
public abstract class Link implements Votable, Created {
    public abstract String id();
    public abstract String name();
    public abstract String author();
    public abstract String title();
    public abstract String url();
    public abstract int score();

    /*public static Link create(int ups, int downs, long created, long createdUTC,
                              String author, String title, String url, int score) {
        return new AutoValue_Link(ups, downs, created, createdUTC, author, title, url, score);
    }*/

    public static JsonAdapter<Link> jsonAdapter(Moshi moshi) {
        return new AutoValue_Link.MoshiJsonAdapter(moshi);
    }

    public static TypeAdapter<Link> typeAdapter(Gson gson) {
        return new AutoValue_Link.GsonTypeAdapter(gson);
    }
}
