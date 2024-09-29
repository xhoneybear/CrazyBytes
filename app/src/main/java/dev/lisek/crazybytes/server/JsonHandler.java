package dev.lisek.crazybytes.server;

import com.google.gson.Gson;

import dev.lisek.crazybytes.entity.ExportProfile;
import dev.lisek.crazybytes.entity.Profile;

public class JsonHandler {

    private static final Gson gson = new Gson();

    private JsonHandler() {}

    public static ExportProfile from(String json) {
        ExportProfile profile = gson.fromJson(json, ExportProfile.class);
        profile.init();
        return profile;
    }

    public static String to(Profile profile) {
        return gson.toJson(profile);
    }
}
