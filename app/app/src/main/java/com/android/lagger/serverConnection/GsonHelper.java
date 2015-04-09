package com.android.lagger.serverConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Ewelina Klisowska on 2015-04-09.
 */
public class GsonHelper {
    public Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DotNetDateDeserializer());
        builder.registerTypeAdapter(Date.class, new DotNetDateSerializer());
        return builder.create();
    }

    public class DotNetDateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            try {
                String dateStr = json.getAsString();

//                if (dateStr != null) dateStr = dateStr.replace("/Date(", "");
//                if (dateStr != null) dateStr = dateStr.substring("/Date(", "");
//                if (dateStr != null) dateStr = dateStr.replace("+0200)/", "");
//                if (dateStr != null) dateStr = dateStr.replace(")/", "");
//                long time = Long.parseLong(dateStr);
                long time = Long.parseLong(dateStr.substring(6, dateStr.length() - 7));
                return new Date(time);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }
    }

    public class DotNetDateSerializer implements JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) {
            if (date == null)
                return null;

            String dateStr = "/Date(" + date.getTime() + ")/";
            return new JsonPrimitive(dateStr);
        }
    }
}
