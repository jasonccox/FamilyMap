package com.jasoncarloscox.familymap.model;

import com.jasoncarloscox.familymap.R;

public enum Color {

    // IMPORTANT: the order of the enum values must stay in sync with the
    // colors_array in the strings.xml file

    RED(R.color.colorRed),
    ORANGE(R.color.colorOrange),
    YELLOW(R.color.colorYellow),
    GREEN(R.color.colorGreen),
    BLUE(R.color.colorBlue),
    PURPLE(R.color.colorPurple),
    PINK(R.color.colorPink),
    WHITE(R.color.colorWhite),
    BLACK(R.color.colorBlack);

    private int resource;

    public int getResource() {
        return resource;
    }

    private Color(int resource) {
        this.resource = resource;
    }
}