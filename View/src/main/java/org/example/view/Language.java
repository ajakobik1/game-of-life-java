package org.example.view;

public enum Language {
    EN("en", "UK", "Language_en"),
    PL("pl", "PL", "Language_pl"),
    ES("es", "ES", "Language_es"),
    TR("tr", "TR", "Language_tr");


    private final String country;
    private final String language;
    private final String path;

    Language(String language, String country, String path) {
        this.path = path;
        this.country = country;
        this.language = language;
    }

    public String getPath() {
        return path;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }
}
