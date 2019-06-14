package com.jasoncarloscox.familymap.model;

public class Settings {

    private boolean showLifeLines = true;
    private boolean showTreeLines = true;
    private boolean showSpouseLines = true;
    private Color lifeLineColor = Color.BLUE;
    private Color treeLineColor = Color.GREEN;
    private Color spouseLineColor = Color.RED;
    private MapType mapType = MapType.NORMAL;

    private boolean oldShowLifeLines;
    private boolean oldShowTreeLines;
    private boolean oldShowSpouseLines;
    private Color oldLifeLineColor;
    private Color oldTreeLineColor;
    private Color oldSpouseLineColor;
    private MapType oldMapType;

    public Settings() {
        resetAltered();
    }

    public boolean showLifeLines() {
        return showLifeLines;
    }

    public void setShowLifeLines(boolean showLifeLines) {
        this.showLifeLines = showLifeLines;
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

    public Color getLifeLineColor() {
        return lifeLineColor;
    }

    public void setLifeLineColor(Color lifeLineColor) {
        this.lifeLineColor = lifeLineColor;
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

    public boolean isAltered() {
        return areLinesAltered() || isMapTypeAltered();
    }

    public void resetAltered() {
        oldShowLifeLines = showLifeLines;
        oldShowTreeLines = showTreeLines;
        oldShowSpouseLines = showSpouseLines;
        oldLifeLineColor = lifeLineColor;
        oldTreeLineColor = treeLineColor;
        oldSpouseLineColor = spouseLineColor;
        oldMapType = mapType;
    }

    public boolean areLinesAltered() {
        return isLifeLineVisibilityAltered() ||
                isTreeLineVisibilityAltered() ||
                isSpouseLineVisibilityAltered() ||
                isLifeLineColorAltered() ||
                isTreeLineColorAltered() ||
                isSpouseLineColorAltered();
    }

    public boolean isLifeLineVisibilityAltered() {
        return showLifeLines != oldShowLifeLines;
    }

    public boolean isTreeLineVisibilityAltered() {
        return showTreeLines != oldShowTreeLines;
    }

    public boolean isSpouseLineVisibilityAltered() {
        return showSpouseLines != oldShowSpouseLines;
    }

    public boolean isLifeLineColorAltered() {
        return lifeLineColor != oldLifeLineColor;
    }

    public boolean isTreeLineColorAltered() {
        return treeLineColor != oldTreeLineColor;
    }

    public boolean isSpouseLineColorAltered() {
        return spouseLineColor != oldSpouseLineColor;
    }

    public boolean isMapTypeAltered() {
        return mapType != oldMapType;
    }
}
