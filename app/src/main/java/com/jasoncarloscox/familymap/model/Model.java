package com.jasoncarloscox.familymap.model;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A singleton class storing the app's data.
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
    private EventFilter filter;
    private Settings settings;
    private Map<Event, Marker> markersByEvent = new HashMap<>();

    private Model() {
        settings = new Settings();
    }

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
        filter = new EventFilter(tree.getEventTypes());
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

    public FamilyTree getTree() {
        return tree;
    }

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        clear();
    }

    public void login(User user) {
        setUser(user);
    }

    public void addMarker(Marker marker, Event event) {
        markersByEvent.put(event, marker);
    }

    public Marker getMarker(Event event) {
        return markersByEvent.get(event);
    }

    public void refreshMarkers() {
        for (Event event : markersByEvent.keySet()) {
            markersByEvent.get(event).setVisible(filter.showEvent(event));
        }
    }

    public EventFilter getFilter() {
        return filter;
    }

    public Settings getSettings() {
        return settings;
    }

    public void clear() {
        tree = new FamilyTree();
        user = null;
        filter = null;
        settings = new Settings();
    }

}
