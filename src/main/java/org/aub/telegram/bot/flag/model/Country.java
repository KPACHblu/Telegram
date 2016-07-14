package org.aub.telegram.bot.flag.model;

public class Country {
    private String name;
    private String capital;
    private String region;
    private String flagPath;

    public Country(String name, String capital, String region, String flagPath) {
        this.name = name;
        this.capital = capital;
        this.region = region;
        this.flagPath = flagPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlagPath() {
        return flagPath;
    }

    public void setFlagPath(String flagPath) {
        this.flagPath = flagPath;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
