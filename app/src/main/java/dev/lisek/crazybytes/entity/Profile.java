package dev.lisek.crazybytes.entity;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import dev.lisek.crazybytes.config.Config;
import dev.lisek.crazybytes.ui.element.ProfileCard;

public class Profile {
    public String name;
    public String avatar;
    public int exp;
    public int games;
    public int wins;
    public transient ProfileCard card;

    public Profile(String name, String avatar, int exp, int games, int wins) {
        this.name = name;
        this.avatar = avatar;
        this.exp = exp;
        this.games = games;
        this.wins = wins;
        this.card = new ProfileCard(this);
    }
    public Profile() {
        this("guest", "file:", 0, 0, 0);
    }

    public static final Profile init(String data) {
        try {
            if (data == null) {
                data = Files.readString(Paths.get(URI.create(Config.DIR + "account.json")));
            }
            Profile profile = Config.gson.fromJson(data, Profile.class);
            profile.card = new ProfileCard(profile);
            return profile;
        } catch (IOException e) {
            try {
                Profile profile = new Profile();
                Files.writeString(Paths.get(URI.create(Config.DIR + "account.json")), Config.gson.toJson(profile));
                return profile;
            } catch (IOException er) {
                er.printStackTrace();
                return null;
            }
        }
    }
    public static final Profile init() {
        return init(null);
    }

    public void update(String key, String value) {
        switch (key) {
            case "name" -> this.name = value;
            case "avatar" -> this.avatar = value;
            case "exp" -> this.exp += Integer.parseInt(value);
            case "games" -> this.games++;
            case "wins" -> this.wins++;
        }
        try {
            Files.writeString(Paths.get(URI.create(Config.DIR + "account.json")), Config.gson.toJson(this));
            this.card.update(key, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void update(String key) {
        update(key, null);
    }
}
