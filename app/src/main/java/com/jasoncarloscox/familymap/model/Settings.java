package com.jasoncarloscox.familymap.model;

class Settings {

    private boolean showLifeLines = true;
    private boolean showTreeLines = true;
    private boolean showSpouseLines = true;
    private LineSetting.Color lifeLineColor = LineSetting.Color.BLUE;
    private LineSetting.Color treeLineColor = LineSetting.Color.GREEN;
    private LineSetting.Color spouseLineColor = LineSetting.Color.RED;
    private EventMap.Type mapType = EventMap.Type.NORMAL;

    // store old values to know when there are changes
    private boolean oldShowLifeLines;
    private boolean oldShowTreeLines;
    private boolean oldShowSpouseLines;
    private LineSetting.Color oldLifeLineColor;
    private LineSetting.Color oldTreeLineColor;
    private LineSetting.Color oldSpouseLineColor;
    private EventMap.Type oldMapType;

    protected Settings() {
        resetAltered();
    }

    /**
     * @return whether lines should be shown connecting each person's life events
     */
    protected boolean shouldShowLifeLines() {
        return showLifeLines;
    }

    /**
     * @param shouldShow whether lines should be shown connecting each person's
     *                   life events
     */
    protected void setShouldShowLifeLines(boolean shouldShow) {
        this.showLifeLines = shouldShow;
    }

    /**
     * @return whether lines should be shown connecting each person to his/her
     *         ancestors
     */
    protected boolean shouldShowTreeLines() {
        return showTreeLines;
    }

    /**
     * @param shouldShow whether lines should be shown connecting each person to
     *                   his/her ancestors
     */
    protected void setShouldShowTreeLines(boolean shouldShow) {
        this.showTreeLines = shouldShow;
    }

    /**
     * @return whether lines should be shown connecting each person to his/her
     *         spouse
     */
    protected boolean shouldShowSpouseLines() {
        return showSpouseLines;
    }

    /**
     * @param shouldShow whether lines should be shown connecting each person to
     *                   his/her spouse
     */
    protected void setShouldShowSpouseLines(boolean shouldShow) {
        this.showSpouseLines = shouldShow;
    }

    /**
     * @return the type of map background to display
     */
    protected EventMap.Type getMapType() {
        return mapType;
    }

    /**
     * @param type the type of map background to display
     */
    protected void setMapType(EventMap.Type type) {
        this.mapType = type;
    }

    /**
     * @return the color with which to draw lines connecting each person's events
     */
    protected LineSetting.Color getLifeLineColor() {
        return lifeLineColor;
    }

    /**
     * @param color the color with which to draw lines connecting each person's
     *              events
     */
    protected void setLifeLineColor(LineSetting.Color color) {
        this.lifeLineColor = color;
    }

    /**
     * @return the color with which to draw lines connecting each person to his/
     *         her ancestors
     */
    protected LineSetting.Color getTreeLineColor() {
        return treeLineColor;
    }

    /**
     * @param color the color with which to draw lines connecting each person to
     *              his/her ancestors
     */
    protected void setTreeLineColor(LineSetting.Color color) {
        this.treeLineColor = color;
    }

    /**
     * @return the color with which to draw lines connecting each person to his/
     *         her spouse
     */
    protected LineSetting.Color getSpouseLineColor() {
        return spouseLineColor;
    }

    /**
     * @param color the color with which to draw lines connecting each person to
     *              his/her spouse
     */
    protected void setSpouseLineColor(LineSetting.Color color) {
        this.spouseLineColor = color;
    }

    /**
     * @return whether any of the settings have changed since resetAltered() was
     *         last called
     */
    protected boolean isAltered() {
        return areLinesAltered() || isMapTypeAltered();
    }

    /**
     * @return whether any of the line settings have changed since resetAltered()
     *         was last called
     */
    protected boolean areLinesAltered() {
        return showLifeLines != oldShowLifeLines ||
                showTreeLines != oldShowTreeLines ||
                showSpouseLines != oldShowSpouseLines ||
                treeLineColor != oldTreeLineColor ||
                lifeLineColor != oldLifeLineColor ||
                spouseLineColor != oldSpouseLineColor;
    }

    /**
     * @return whether the map type setting has changed since resetAltered() was
     *         last called
     */
    protected boolean isMapTypeAltered() {
        return mapType != oldMapType;
    }

    /**
     * Sets a new baseline from which to determine if the settings have been
     * altered.
     */
    protected void resetAltered() {
        oldShowLifeLines = showLifeLines;
        oldShowTreeLines = showTreeLines;
        oldShowSpouseLines = showSpouseLines;
        oldLifeLineColor = lifeLineColor;
        oldTreeLineColor = treeLineColor;
        oldSpouseLineColor = spouseLineColor;
        oldMapType = mapType;
    }
}
