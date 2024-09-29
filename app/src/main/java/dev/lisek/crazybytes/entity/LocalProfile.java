package dev.lisek.crazybytes.entity;

import java.util.UUID;
import java.util.prefs.Preferences;

public class LocalProfile implements Profile {

    public final Preferences prefs;

    public final ProfileCard card;

    public final ExportProfile export;

    public LocalProfile() {
        this(Preferences.userNodeForPackage(LocalProfile.class));
    }
    public LocalProfile(Preferences prefs) {
        this.prefs = prefs;
        if (null == this.prefs.get("uuid", null)) {
            this.prefs.put("uuid", UUID.randomUUID().toString());
            this.prefs.put("name", "guest");
            this.prefs.put("avatar", "file:");
            this.prefs.putInt("exp", 0);
            this.prefs.putInt("games", 0);
            this.prefs.putInt("wins", 0);
        }
        this.card = new ProfileCard(this);
        this.export = new ExportProfile(this);
    }

    public String uuid() {
        return prefs.get("uuid", null);
    }
    public String name() {
        return prefs.get("name", null);
    }
    public String avatar() {
        return prefs.get("avatar", null);
    }
    public int exp() {
        return prefs.getInt("exp", 0);
    }
    public int games() {
        return prefs.getInt("games", 0);
    }
    public int wins() {
        return prefs.getInt("wins", 0);
    }

    public void update(String key, Object value) {
        try {
            prefs.putInt(key, prefs.getInt(key, 0) + (int) value);
        } catch (ClassCastException e) {
            prefs.put(key, (String) value);
        }
        this.card.update(key);
        this.export.update(key, value);
    }
    public void update(String key) {
        update(key, 1);
    }
}
