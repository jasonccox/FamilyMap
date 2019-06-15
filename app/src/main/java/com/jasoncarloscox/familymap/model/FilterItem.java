package com.jasoncarloscox.familymap.model;

/**
 * Represents a specific piece of data on which events can be filtered.
 */
public class FilterItem {

    private String title;
    private String description;
    private boolean initiallyFiltered;
    private Listener listener;

    /**
     * Implement this interface in order to run custom code when the filter is
     * turned on or off.
     */
    public interface Listener {

        /**
         * Called when the filter is turned on or off.
         *
         * @param filtered whether the filter is on (i.e. Events matching the
         *                 filter should be shown)
         */
        void onFilterStatusChange(boolean filtered);
    }

    /**
     * Creates a new FilterItem.
     *
     * @param title the item's title
     * @param description the item's description, written such that "Show/Hide..."
     *                    makes sense
     * @param initiallyFiltered whether the filter is initially on
     */
    public FilterItem(String title, String description, boolean initiallyFiltered) {
        this.title = title;
        this.description = description;
        this.initiallyFiltered = initiallyFiltered;
        this.listener = listener;
    }

    /**
     * @return the item's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the item's description, written such that "Show/Hide..." makes
     *         sense
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return whether the filter is initially on
     */
    public boolean isInitiallyFiltered() {
        return initiallyFiltered;
    }

    /**
     * @return an instance of a FilterItem.Listener to be notified when the
     *         filter's status changes
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * @param listener an instance of a FilterItem.Listener to be notified when
     *        the filter's status changes
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
