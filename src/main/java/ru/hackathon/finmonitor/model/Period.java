package ru.hackathon.finmonitor.model;

public enum Period {
    WEEK("Неделя"),
    MONTH("Месяц"),
    QUARTER("Квартал"),
    YEAR("Год");

    private final String displayName;

    Period(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}