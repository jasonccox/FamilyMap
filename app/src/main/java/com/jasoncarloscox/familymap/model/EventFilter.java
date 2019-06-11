package com.jasoncarloscox.familymap.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EventFilter {

    private Map<String, Boolean> showTypes = new HashMap<>();
    private boolean showMaternalSide = true;
    private boolean showPaternalSide = true;
    private boolean showMale = true;
    private boolean showFemale = true;

    public EventFilter(Collection<String> types) {
        for (String type : types) {
            showTypes.put(type, true);
        }
    }

    /**
     * Determines if an event should be shown based on the current filters.
     *
     * @param event an event
     * @return whether the given event should be shown
     */
    public boolean showEvent(Event event) {

        if (!showPaternalSide && !showMaternalSide) {
            return false;
        }

        if (!showMale && !showFemale) {
            return false;
        }

        Person eventPerson = event.getPerson();

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

        if (!showMale && eventPerson.getGender().equals(Gender.MALE)) {
            return false;
        }

        if (!showFemale && eventPerson.getGender().equals(Gender.FEMALE)) {
            return false;
        }

        if (!showType(event.getType())) {
            return false;
        }

        return true;
    }

    /**
     * Determines whether an event type should be shown based on the current
     * filters.
     *
     * @param type an event type
     * @return whether events of the given type should be shown
     */
    public boolean showType(String type) {
        if (!showTypes.containsKey(type)) {
            return false;
        }

        return showTypes.get(type);
    }

    /**
     * Set whether events of a given type should be shown.
     *
     * @param type and event type
     * @param showType whether to show events of the given type
     */
    public void setShowType(String type, boolean showType) {
        showTypes.put(type, showType);
    }

    /**
     * @return whether events on rootPerson's maternal side should be shown
     */
    public boolean showMaternalSide() {
        return showMaternalSide;
    }

    /**
     * @param showMaternalSide whether events on rootPerson's maternal side
     *                         should be shown
     */
    public void setShowMaternalSide(boolean showMaternalSide) {
        this.showMaternalSide = showMaternalSide;
    }

    /**
     * @return whether events on rootPerson's paternal side should be shown
     */
    public boolean showPaternalSide() {
        return showPaternalSide;
    }

    /**
     * @param showPaternalSide whether events on rootPerson's paternal side
     *                         should be shown
     */
    public void setShowPaternalSide(boolean showPaternalSide) {
        this.showPaternalSide = showPaternalSide;
    }

    /**
     * @return whether male events should be shown
     */
    public boolean showMale() {
        return showMale;
    }

    /**
     * @param showMale whether male events should be shown
     */
    public void setShowMale(boolean showMale) {
        this.showMale = showMale;
    }

    /**
     * @return whether female events should be shown
     */
    public boolean showFemale() {
        return showFemale;
    }

    /**
     * @param showFemale whether female events should be shown
     */
    public void setShowFemale(boolean showFemale) {
        this.showFemale = showFemale;
    }
}
