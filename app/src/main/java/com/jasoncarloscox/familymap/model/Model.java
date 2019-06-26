package com.jasoncarloscox.familymap.model;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A singleton class storing the app's data and providing methods to access it.
 */
public class Model {

    private static Model instance;

    /**
     * @return an instance of the Model class
     */
    public static Model instance() {
        if (instance == null) {
            instance = new Model();
        }

        return instance;
    }

    private FamilyTree tree = new FamilyTree();
    private User user = null;
    private EventFilter filter = new EventFilter();
    private Settings settings;
    private SharedMapState mapState = new SharedMapState(this);

    /**
     * Creates a new Model.
     */
    private Model() {
        settings = new Settings();
    }

    /**
     * Mark the specified user as logged in
     * @param user the user to log in
     */
    public void login(User user) {
        this.user = user;
    }

    /**
     * Mark the current user as logged out, clearing all data and settings.
     */
    public void logout() {
        clear();
        filter = new EventFilter();
        settings = new Settings();
        mapState = new SharedMapState(this);
    }

    /**
     * @return whether a user is logged in
     */
    public boolean isLoggedIn() {
        return user != null;
    }

    /**
     * @return the currently logged in user
     */
    public User getUser() {
        return user;
    }

    /**
     * Clears all user, person, and event data.
     */
    public void clear() {
        tree = new FamilyTree();
        user = null;
    }

    // =========================================================================
    // ========================= FAMILY TREE ACCESS ============================
    // =========================================================================

    /**
     * Loads persons and events into the family tree. This includes setting each
     * person's father, mother, and children and whether the person is on the
     * maternal or paternal side of the rootPerson's tree. It also includes
     * attaching each event to its person.
     *
     * All persons and events will be cleared from the tree before the new ones
     * are loaded.
     *
     * @param persons the persons to load
     * @param rootPersonId the id of the root person - this person must be
     *                     included in persons
     * @param events the events to load - each must belong to a person in persons
     */
    public void load(Collection<Person> persons, String rootPersonId,
                     Collection<Event> events) {

        tree.load(persons, rootPersonId, events);
        filter.setEventTypes(getEventTypes(), true);

        mapState.invalidateMarkerColors();
        mapState.invalidateMarkers();
        mapState.invalidateLines();
    }

    /**
     * Gets a person with the designated id.
     *
     * @param id the id of the person to get
     * @return the person with the given id, or null if there is no such person
     */
    public Person getPerson(String id) {
        return tree.getPerson(id);
    }

    /**
     * @return all of the persons in the family tree
     */
    public Collection<Person> getPersons() {
        return tree.getPersons();
    }

    /**
     * @return the root of the family tree
     */
    public Person getRootPerson() {
        return tree.getRootPerson();
    }

    /**
     * Gets a event with the designated id.
     *
     * @param id the id of the event to get
     * @return the event with the given id, or null if there is no such event
     */
    public Event getEvent(String id) {
        return tree.getEvent(id);
    }

    /**
     * @return all the events in this tree
     */
    public Collection<Event> getEvents() {
        return tree.getEvents();
    }

    /**
     * @return the types of events in the family tree
     */
    public Set<String> getEventTypes() {
        return tree.getEventTypes();
    }

    /**
     * Finds all the persons and events that match some or all of the words in
     * the query string.
     *
     * @param query the query string
     * @return a list of matching Persons and Events
     */
    public List<Object> searchPersonsAndEvents(String query) {
        Set<Object> results = new HashSet<>();

        for (String word : query.toLowerCase().split("\\W")) {
            results.addAll(searchPersonAndAncestors(word, getRootPerson()));
            results.addAll(searchPersonAndAncestors(word, getRootPerson().getSpouse()));
        }

        List<Object> resultList = new ArrayList<>();
        resultList.addAll(results);
        return resultList;
    }

    // =========================================================================
    // ======================= SHARED MAP STATE ACCESS =========================
    // =========================================================================

    /**
     * Updates the given map based on the current shared map state.
     *
     * @param map the map to be updated
     */
    public void pushMapUpdatesTo(EventMap map, Resources res) {
        if (filter.isAltered()) {
            mapState.invalidateMarkers();
            mapState.invalidateLines();
        }

        if (settings.areLinesAltered()) {
            mapState.invalidateLines();
        }

        if (settings.isMapTypeAltered()) {
            mapState.invalidateMapType();
        }

        filter.resetAltered();
        settings.resetAltered();

        mapState.pushMapUpdatesTo(map, res);
    }

    // =========================================================================
    // ========================= EVENT FILTER ACCESS ===========================
    // =========================================================================

    /**
     * Reduces a Collection of Events to a List of Events that match the filter.
     *
     * @param events the Events to be filtered
     * @return the Events that should be displayed
     */
    public List<Event> filter(Collection<Event> events) {
        return filter.filter(events);
    }

    /**
     * Determines if an event should be shown based on the current filters.
     *
     * @param event an event
     * @return whether the given event should be shown
     */
    public boolean shouldShow(Event event) {
        return filter.shouldShow(event);
    }

    /**
     * Determines whether an event type should be shown based on the current
     * filters. Event types that the filter doesn't recognize return false;
     *
     * @param eventType an event type
     * @return whether events of the given type should be shown
     */
    public boolean shouldShow(String eventType) {
        return filter.shouldShow(eventType);
    }

    /**
     * Set whether events of a given type should be shown.
     *
     * @param eventType an event type
     * @param shouldShow whether to show events of the given type
     */
    public void setShouldShow(String eventType, boolean shouldShow) {
        filter.setShouldShow(eventType, shouldShow);
    }

    /**
     * @return whether events on rootPerson's maternal side should be shown
     */
    public boolean shouldShowMaternalSide() {
        return filter.shouldShowMaternalSide();
    }

    /**
     * @param shouldShow whether events on rootPerson's maternal side
     *                   should be shown
     */
    public void setShouldShowMaternalSide(boolean shouldShow) {
        filter.setShouldShowMaternalSide(shouldShow);
    }

    /**
     * @return whether events on rootPerson's paternal side should be shown
     */
    public boolean shouldShowPaternalSide() {
        return filter.shouldShowPaternalSide();
    }

    /**
     * @param shouldShow whether events on rootPerson's paternal side
     *                   should be shown
     */
    public void setShouldShowPaternalSide(boolean shouldShow) {
        filter.setShouldShowPaternalSide(shouldShow);
    }

    /**
     * @return whether male events should be shown
     */
    public boolean shouldShowMale() {
        return filter.shouldShowMale();
    }

    /**
     * @param shouldShow whether male events should be shown
     */
    public void setShouldShowMale(boolean shouldShow) {
        filter.setShouldShowMale(shouldShow);
    }

    /**
     * @return whether female events should be shown
     */
    public boolean shouldShowFemale() {
        return filter.shouldShowFemale();
    }

    /**
     * @param shouldShow whether female events should be shown
     */
    public void setShouldShowFemale(boolean shouldShow) {
        filter.setShouldShowFemale(shouldShow);
    }

    // =========================================================================
    // =========================== SETTINGS ACCESS =============================
    // =========================================================================

    /**
     * @return whether lines should be shown connecting each person's life events
     */
    public boolean shouldShowLifeLines() {
        return settings.shouldShowLifeLines();
    }

    /**
     * @param shouldShow whether lines should be shown connecting each person's
     *                   life events
     */
    public void setShouldShowLifeLines(boolean shouldShow) {
        settings.setShouldShowLifeLines(shouldShow);
    }

    /**
     * @return whether lines should be shown connecting each person to his/her
     *         ancestors
     */
    public boolean shouldShowTreeLines() {
        return settings.shouldShowTreeLines();
    }

    /**
     * @param shouldShow whether lines should be shown connecting each person to
     *                   his/her ancestors
     */
    public void setShouldShowTreeLines(boolean shouldShow) {
        settings.setShouldShowTreeLines(shouldShow);
    }

    /**
     * @return whether lines should be shown connecting each person to his/her
     *         spouse
     */
    public boolean shouldShowSpouseLines() {
        return settings.shouldShowSpouseLines();
    }

    /**
     * @param shouldShow whether lines should be shown connecting each person to
     *                   his/her spouse
     */
    public void setShouldShowSpouseLines(boolean shouldShow) {
        settings.setShouldShowSpouseLines(shouldShow);
    }

    /**
     * @return the type of map background to display
     */
    public EventMap.Type getMapType() {
        return settings.getMapType();
    }

    /**
     * @param type the type of map background to display
     */
    public void setMapType(EventMap.Type type) {
        settings.setMapType(type);
    }

    /**
     * @return the color with which to draw lines connecting each person's events
     */
    public LineSetting.Color getLifeLineColor() {
        return settings.getLifeLineColor();
    }

    /**
     * @param color the color with which to draw lines connecting each person's
     *              events
     */
    public void setLifeLineColor(LineSetting.Color color) {
        settings.setLifeLineColor(color);
    }

    /**
     * @return the color with which to draw lines connecting each person to his/
     *         her ancestors
     */
    public LineSetting.Color getTreeLineColor() {
        return settings.getTreeLineColor();
    }

    /**
     * @param color the color with which to draw lines connecting each person to
     *              his/her ancestors
     */
    public void setTreeLineColor(LineSetting.Color color) {
        settings.setTreeLineColor(color);
    }

    /**
     * @return the color with which to draw lines connecting each person to his/
     *         her spouse
     */
    public LineSetting.Color getSpouseLineColor() {
        return settings.getSpouseLineColor();
    }

    /**
     * @param color the color with which to draw lines connecting each person to
     *              his/her spouse
     */
    public void setSpouseLineColor(LineSetting.Color color) {
        settings.setSpouseLineColor(color);
    }

    // =========================================================================
    // ========================++++=== HELPERS ========++++=====================
    // =========================================================================

    /**
     * Recursively searches a person and his/her ancestors and their associated
     * events for matches to the query.
     *
     * @param query the search query in all lowercase
     * @param person the root person to start the recursive search
     * @return the search results (Persons and Events)
     */
    private List<Object> searchPersonAndAncestors(String query, Person person) {
        List<Object> results = new ArrayList<>();

        if (person == null) {
            return results;
        }

        if (isSearchMatch(query, person)) {
            results.add(person);
        }

        for (Event event : filter(person.getEvents())) {
            if (isSearchMatch(query, event)) {
                results.add(event);
            }
        }

        results.addAll(searchPersonAndAncestors(query, person.getFather()));
        results.addAll(searchPersonAndAncestors(query, person.getMother()));

        return results;
    }

    /**
     * @param query a search query in all lowercase
     * @param event an event
     * @return whether the event matches the query
     */
    private boolean isSearchMatch(String query, Event event) {
        if (event.getType() != null && event.getType().toLowerCase().contains(query)) {
            return true;
        }

        if (event.getCity() != null && event.getCity().toLowerCase().contains(query)) {
            return true;
        }

        if (event.getCountry() != null && event.getCountry().toLowerCase().contains(query)) {
            return true;
        }

        if (String.valueOf(event.getYear()).contains(query)) {
            return true;
        }

        return false;
    }

    /**
     * @param query a search query in all lowercase
     * @param person a person
     * @return whether the person matches the query
     */
    private boolean isSearchMatch(String query, Person person) {
        if (person.getFullName().toLowerCase().contains(query)) {
            return true;
        }

        return false;
    }

    /**
     * Only to be used from tests!
     *
     * Gets a new Model instance. Future calls to instance() will return this
     * new instance
     */
    protected static Model newInstance() {
        instance = new Model();

        return instance;
    }
}
