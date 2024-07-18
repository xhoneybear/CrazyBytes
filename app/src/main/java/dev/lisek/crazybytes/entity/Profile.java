package dev.lisek.crazybytes.entity;

import java.util.prefs.Preferences;

public class Profile {

    public final Preferences prefs;

    public final ProfileCard card;

    public ProfileCard editable = null;

    public Profile() {
        this(Preferences.userNodeForPackage(Profile.class));
    }
    public Profile(Preferences prefs) {
        this.prefs = prefs;
        if (null == this.prefs.get("avatar", null)) {
            this.prefs.put("name", "guest");
            this.prefs.put("avatar", "file:");
            this.prefs.putInt("exp", 0);
            this.prefs.putInt("games", 0);
            this.prefs.putInt("wins", 0);
        }
        this.card = new ProfileCard(this);
    }

    public ProfileCard editableCard() {
        if (this.editable == null) {
            this.editable = new ProfileCard(this, true);
        }
        return this.editable;
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
        if (this.editable != null) {
            this.editable.update(key);
        }
    }
    public void update(String key) {
        update(key, 1);
    }
}
