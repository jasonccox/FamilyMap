package com.jasoncarloscox.familymap.model;

import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores information about which events should be displayed.
 */
class EventFilter {

    private Map<String, Boolean> showTypes = new HashMap<>();
    private boolean showMaternalSide = true;
    private boolean showPaternalSide = true;
    private boolean showMale = true;
    private boolean showFemale = true;

    // track old values to know when changes have been made
    private Map<String, Boolean> oldShowTypes = new HashMap<>();
    private boolean oldShowMaternalSide;
    private boolean oldShowPaternalSide;
    private boolean oldShowMale;
    private boolean oldShowFemale;

    /**
     * Creates a new Event filter with all events shown by default.
     */
    protected EventFilter() {
        resetAltered();
    }

    /**
     * Sets the given types as the list of filterable event types. If any of the
     * given types are already filterable, their status will not be changed. Any
     * event types not given will be removed.
     *
     * @param eventTypes a collection of event types to be shown/hidden
     * @param shouldShow whether to show the events of the given types by default
     */
    protected void setEventTypes(Collection<String> eventTypes,
                                 boolean shouldShow) {

        for (String type : showTypes.keySet()) {
            if (!eventTypes.contains(type)) {
                showTypes.remove(type);
            }
        }

        for (String type : eventTypes) {
            if (!showTypes.containsKey(type)) {
                showTypes.put(type, shouldShow);
            }
        }
    }

    /**
     * Reduces a Collection of Events to a List of Events that match the filter.
     *
     * @param events the Events to be filtered
     * @return the Events that should be displayed
     */
    protected List<Event> filter(Collection<Event> events) {
        List<Event> filtered = new ArrayList<>();

        for (Event event : events) {
            if (shouldShow(event)) {
                filtered.add(event);
            }
        }

        return filtered;
    }

    /**
     * Determines if an event should be shown based on the current filters.
     *
     * @param event an event
     * @return whether the given event should be shown
     */
    protected boolean shouldShow(Event event) {
        Person eventPerson = event.getPerson();

        if (!showMale && eventPerson.getGender().equals(Gender.MALE)) {
            return false;
        }

        if (!showFemale && eventPerson.getGender().equals(Gender.FEMALE)) {
            return false;
        }

        // the rootPerson will be on both paternal and maternal side, so we need
        // to account for that special case

        if (!showPaternalSide && eventPerson.isPaternalSide() &&
                !eventPerson.isMaternalSide()) {
            return false;
        }

        if (!showMaternalSide && eventPerson.isMaternalSide() &&
                !eventPerson.isPaternalSide()) {
            return false;
        }

        if (!shouldShow(event.getType())) {
            return false;
        }

        return true;
    }

    /**
     * Determines whether an event type should be shown based on the current
     * filters. Event types that the filter doesn't recognize return true;
     *
     * @param eventType an event type
     * @return whether events of the given type should be shown
     */
    protected boolean shouldShow(String eventType) {
        if (!showTypes.containsKey(eventType)) {
            return true;
        }

        return showTypes.get(eventType);
    }

    /**
     * Set whether events of a given type should be shown.
     *
     * @param eventType and event type
     * @param shouldShow whether to show events of the given type
     */
    protected void setShouldShow(String eventType, boolean shouldShow) {
        showTypes.put(eventType, shouldShow);
    }

    /**
     * @return whether events on rootPerson's maternal side should be shown
     */
    protected boolean shouldShowMaternalSide() {
        return showMaternalSide;
    }

    /**
     * @param shouldShow whether events on rootPerson's maternal side
     *                   should be shown
     */
    protected void setShouldShowMaternalSide(boolean shouldShow) {
        this.showMaternalSide = shouldShow;
    }

    /**
     * @return whether events on rootPerson's paternal side should be shown
     */
    protected boolean shouldShowPaternalSide() {
        return showPaternalSide;
    }

    /**
     * @param shouldShow whether events on rootPerson's paternal side
     *                   should be shown
     */
    protected void setShouldShowPaternalSide(boolean shouldShow) {
        this.showPaternalSide = shouldShow;
    }

    /**
     * @return whether male events should be shown
     */
    protected boolean shouldShowMale() {
        return showMale;
    }

    /**
     * @param shouldShow whether male events should be shown
     */
    protected void setShouldShowMale(boolean shouldShow) {
        this.showMale = shouldShow;
    }

    /**
     * @return whether female events should be shown
     */
    protected boolean shouldShowFemale() {
        return showFemale;
    }

    /**
     * @param shouldShow whether female events should be shown
     */
    protected void setShouldShowFemale(boolean shouldShow) {
        this.showFemale = shouldShow;
    }

    /**
     * @return whether any of the filters have been altered since resetAltered()
     *         was last called
     */
    protected boolean isAltered() {
        for (String type : showTypes.keySet()) {
            if (showTypes.get(type) != oldShowTypes.get(type)) {
                return true;
            }
        }

        return oldShowPaternalSide != showPaternalSide ||
               oldShowMaternalSide != showMaternalSide ||
               oldShowMale != showMale ||
               oldShowFemale != showFemale;
    }

    /**
     * Sets the filter's current state as the base line for determining if it
     * has been altered.
     */
    protected void resetAltered() {
        for (String type : showTypes.keySet()) {
            oldShowTypes.put(type, showTypes.get(type));
        }

        oldShowPaternalSide = showPaternalSide;
        oldShowMaternalSide = showMaternalSide;
        oldShowMale = showMale;
        oldShowFemale = showFemale;
    }
}
