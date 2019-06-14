package com.jasoncarloscox.familymap.model;

public class FilterItem {

    private String title;
    private String description;
    private boolean checked;
    private Listener listener;

    public interface Listener {
        void onCheckedChange(boolean checked);
    }

    public FilterItem(String title, String description, boolean checked) {
        this.title = title;
        this.description = description;
        this.checked = checked;
        this.listener = listener;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isChecked() {
        return checked;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
