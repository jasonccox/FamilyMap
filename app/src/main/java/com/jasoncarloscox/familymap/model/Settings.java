package com.jasoncarloscox.familymap.model;

public class Settings {

    private boolean showLifeStoryLines = true;
    private boolean showTreeLines = true;
    private boolean showSpouseLines = true;
    private Color lifeStoryLineColor = Color.BLUE;
    private Color treeLineColor = Color.GREEN;
    private Color spouseLineColor = Color.RED;
    private MapType mapType = MapType.NORMAL;

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

    public MapType getMapType() {
        return mapType;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public Color getLifeStoryLineColor() {
        return lifeStoryLineColor;
    }

    public void setLifeStoryLineColor(Color lifeStoryLineColor) {
        this.lifeStoryLineColor = lifeStoryLineColor;
    }

    public Color getTreeLineColor() {
        return treeLineColor;
    }

    public void setTreeLineColor(Color treeLineColor) {
        this.treeLineColor = treeLineColor;
    }

    public Color getSpouseLineColor() {
        return spouseLineColor;
    }

    public void setSpouseLineColor(Color spouseLineColor) {
        this.spouseLineColor = spouseLineColor;
    }
}
