package com.jasoncarloscox.familymap.model;

public class Settings {

    private boolean showLifeStoryLines = true;
    private boolean showTreeLines = true;
    private boolean showSpouseLines = true;
    private String mapType;

    public Settings() {}

    public boolean showLifeStoryLines() {
        return showLifeStoryLines;
    }

    public void setShowLifeStoryLines(boolean showLifeStoryLines) {
        this.showLifeStoryLines = showLifeStoryLines;
    }

    public boolean showTreeLines() {
        return showTreeLines;
    }

    public void setShowTreeLines(boolean showTreeLines) {
        this.showTreeLines = showTreeLines;
    }

    public boolean showSpouseLines() {
        return showSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }
}
