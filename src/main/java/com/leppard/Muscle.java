package com.leppard;

public class Muscle {
    private final String name;
    private final String part;
    private final double percentage;

    public Muscle(String name, String part, double percentage) {
        this.name = name;
        this.part = part;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public String getPart() {
        return part;
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %.2f%%", name, part, percentage);
    }
}
