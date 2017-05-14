package de.iomu.reddit_experiment.data.model;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.lang.Override;
import java.lang.String;


public final class ThingAdapter<T> extends TypeAdapter<Thing<T>> {
    private final TypeAdapter<T> dataAdapter;
    private final TypeAdapter<String> idAdapter;
    private final TypeAdapter<String> nameAdapter;
    private final TypeAdapter<String> kindAdapter;
    private T defaultData = null;
    private String defaultId = null;
    private String defaultName = null;
    private String defaultKind = null;
    public ThingAdapter(Gson gson, TypeToken<? extends Thing<T>> typeToken) {
        ParameterizedType type = (ParameterizedType) typeToken.getType();
        Type[] typeArgs = type.getActualTypeArguments();
        this.dataAdapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(typeArgs[0]));
        this.idAdapter = gson.getAdapter(String.class);
        this.nameAdapter = gson.getAdapter(String.class);
        this.kindAdapter = gson.getAdapter(String.class);
    }
    public ThingAdapter setDefaultData(T defaultData) {
        this.defaultData = defaultData;
        return this;
    }
    public ThingAdapter setDefaultId(String defaultId) {
        this.defaultId = defaultId;
        return this;
    }
    public ThingAdapter setDefaultName(String defaultName) {
        this.defaultName = defaultName;
        return this;
    }
    public ThingAdapter setDefaultKind(String defaultKind) {
        this.defaultKind = defaultKind;
        return this;
    }
    @Override
    public void write(JsonWriter jsonWriter, Thing<T> object) throws IOException {
        if (object == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginObject();
        jsonWriter.name("data");
        dataAdapter.write(jsonWriter, object.data());
        jsonWriter.name("id");
        idAdapter.write(jsonWriter, object.id());
        jsonWriter.name("name");
        nameAdapter.write(jsonWriter, object.name());
        jsonWriter.name("kind");
        kindAdapter.write(jsonWriter, object.kind());
        jsonWriter.endObject();
    }
    @Override
    public Thing<T> read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        if (jsonReader.peek() == JsonToken.STRING) {
            String s = jsonReader.nextString();
            if (s == "") {
                return null;
            } else {
                throw new IllegalStateException("Can't read thing json");
            }
        }
        jsonReader.beginObject();
        T data = this.defaultData;
        String id = this.defaultId;
        String name = this.defaultName;
        String kind = this.defaultKind;
        while (jsonReader.hasNext()) {
            String _name = jsonReader.nextName();
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                continue;
            }
            switch (_name) {
                case "data": {
                    data = dataAdapter.read(jsonReader);
                    break;
                }
                case "id": {
                    id = idAdapter.read(jsonReader);
                    break;
                }
                case "name": {
                    name = nameAdapter.read(jsonReader);
                    break;
                }
                case "kind": {
                    kind = kindAdapter.read(jsonReader);
                    break;
                }
                default: {
                    jsonReader.skipValue();
                }
            }
        }
        jsonReader.endObject();
        return new AutoValue_Thing<>(data, id, name, kind);
    }
}


