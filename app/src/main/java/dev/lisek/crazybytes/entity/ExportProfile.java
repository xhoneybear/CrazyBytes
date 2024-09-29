package dev.lisek.crazybytes.entity;

import java.io.Serializable;
import java.util.UUID;

import dev.lisek.crazybytes.ui.PlayerEntry;

public class ExportProfile implements Profile, Serializable {

    private String uuid;
    private String name;
    private String avatar;
    private int exp;
    private int games;
    private int wins;

    public transient ProfileCard card;
    public transient PlayerEntry entry;

    // Online player constructor
    public ExportProfile(Profile local) {
        this.uuid = UUID.randomUUID().toString(); // local.uuid();
        this.name = local.name();
        this.avatar = local.avatar();
        this.exp = local.exp();
        this.games = local.games();
        this.wins = local.wins();
        this.card = new ProfileCard(this);
        this.entry = new PlayerEntry(this);
    }

    // Local player constructor
    public ExportProfile(String name, String avatar) {
        this.uuid = null;
        this.name = name;
        this.avatar = avatar;
        this.exp = 0;
        this.games = 0;
        this.wins = 0;
        this.card = new ProfileCard(this);
        this.entry = new PlayerEntry(this);
    }

    public void init() {
        this.card = new ProfileCard(this);
        this.entry = new PlayerEntry(this);
    }

    public String uuid() {
        return uuid;
    }
    public String name() {
        return name;
    }
    public String avatar() {
        return avatar;
    }
    public int exp() {
        return exp;
    }
    public int games() {
        return games;
    }
    public int wins() {
        return wins;
    }

    public void update(String key, Object value) {
        switch (key) {
            case "name" -> this.name = (String) value;
            case "avatar" -> this.avatar = (String) value;
            case "exp" -> this.exp += (int) value;
            case "games" -> this.games += (int) value;
            case "wins" -> this.wins += (int) value;
            default -> throw new RuntimeException("Unknown key: " + key);
        }
        this.card.update(key);
        this.entry.update(key);
    }
    public void update(String key) {
        update(key, null);
    }
}
