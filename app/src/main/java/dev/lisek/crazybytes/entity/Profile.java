package dev.lisek.crazybytes.entity;

public interface Profile {
    public String uuid();
    public String name();
    public String avatar();
    public int exp();
    public int games();
    public int wins();
    public void update(String key, Object value);
    public void update(String key);
}
