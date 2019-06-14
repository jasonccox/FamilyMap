package com.jasoncarloscox.familymap.model;

public class LineSetting {

    public interface Listener {
        void onCheckedChange(boolean checked);
        void onColorSelected(Color color);
    }

    private String type;
    private String description;
    private Listener listener;
    private Color initialColor;
    private boolean initiallyChecked;

    public LineSetting(String type, String description, boolean initiallyChecked,
                       Color initialColor) {
        this.type = type;
        this.description = description;
        this.initialColor = initialColor;
        this.initiallyChecked = initiallyChecked;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Color getInitialColor() {
        return initialColor;
    }

    public boolean isInitiallyChecked() {
        return initiallyChecked;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
