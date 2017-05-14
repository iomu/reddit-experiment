package de.iomu.reddit_experiment.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

@AutoValue
public abstract class MoreComments {
    public abstract String id();
    public abstract String name();
    public abstract int count();
    public abstract String parent_id();
    public abstract int depth();
    public abstract List<String> children();

    public static TypeAdapter<MoreComments> typeAdapter(Gson gson) {
        return new AutoValue_MoreComments.GsonTypeAdapter(gson);
    }
}
