package com.jasoncarloscox.familymap.model;

import com.jasoncarloscox.familymap.R;

/**
 * Represents a type of colored line that can be shown/hidden.
 */
public class LineSetting {

    /**
     * Possible colors for the lines
     */
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

        /**
         * @return the resource id corresponding to this color
         */
        public int getResource() {
            return resource;
        }

        /**
         * @param resource the resource id corresponding to this color
         */
        Color(int resource) {
            this.resource = resource;
        }
    }

    /**
     * Implement this interface in order to run custom code when a line type's
     * settings are modified.
     */
    public interface Listener {

        /**
         * Called when the line type's visibility is toggled
         *
         * @param visible whether the line type is visible
         */
        void onVisibilityChanged(boolean visible);

        /**
         * Called when the line type's Color is changed
         *
         * @param color the line type's new Color
         */
        void onColorSelected(Color color);
    }

    private String type;
    private String description;
    private Listener listener;
    private Color initialColor;
    private boolean initialVisibility;

    /**
     * Create's a new LineSetting
     *
     * @param type the type of the line
     * @param description a description of the line, written such that "Show/hide
     *                    lines connecting..." makes sense
     * @param initialVisibility whether the line type is initially visible
     * @param initialColor the line type's initial color
     */
    public LineSetting(String type, String description, boolean initialVisibility,
                       Color initialColor) {
        this.type = type;
        this.description = description;
        this.initialColor = initialColor;
        this.initialVisibility = initialVisibility;
    }

    /**
     * @return the type of the line
     */
    public String getType() {
        return type;
    }

    /**
     * @return a description of the line, written such that "Show/hide lines
     * connecting..." makes sense
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return whether the line type is initially visible
     */
    public Color getInitialColor() {
        return initialColor;
    }

    /**
     * @return the line type's initial color
     */
    public boolean isInitialVisibility() {
        return initialVisibility;
    }

    /**
     * @return an instance of a LineSetting.Listener to be notified when the line
     *         type's settings change
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * @param listener an instance of a LineSetting.Listener to be notified when
     *                 the line type's settings change
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
