package de.iomu.reddit_experiment.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nullable;

@AutoValue
public abstract class Thing<T> {
    public abstract T data();
    @Nullable public abstract String id();
    @Nullable public abstract String name();
    public abstract String kind();

    public static <T> Thing<T> create(T data, String id, String name, String kind) {
        return new AutoValue_Thing<>(data, id, name, kind);
    }

    public static <T> TypeAdapter<Thing<T>> typeAdapter(Gson gson, TypeToken<? extends Thing<T>> token) {
        return new ThingAdapter<>(gson, token);
    }
}
