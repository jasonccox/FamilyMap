package com.jasoncarloscox.familymap.model;

/**
 * Represents an event occurring in one person's life.
 */
public class Event implements Comparable<Event> {

    private final String eventID;
    private String personID;
    private Person person;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     * Creates a new Event.
     * 
     * @param id a unique identifier for this event
     */
    public Event(String id) {
        this.eventID = id;
    }

    /**
     * @return a unique identifier for this event
     */
    public String getId() {
        return eventID;
    }

    /**
     * @return the id of the person in whose life this event occurred
     */
    public String getPersonId() {
        return personID;
    }

    /**
     * @param person the person in whose life this event occurred
     */
    public void setPerson(Person person) {
        this.person = person;
        this.personID = person.getId();
    }

    /**
     * @return the person in whose life this event occurred
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @param personID the id of the person in whose life this event occurred
     */
    public void setPersonId(String personID) {
        this.personID = personID;
    }

    /**
     * @return the latitude at which the event occurred
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude at which the event occurred
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude at which the event occurred
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude at which the event occurred
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the country in which the event occurred
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country in which the event occurred
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the city in which the event occurred
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city in which the event occurred
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the event's type, in all lowercase
     */
    public String getType() {
        if (eventType != null) {
            return eventType.toLowerCase();
        }

        return null;
    }

    /**
     * @param type the event's type 
     */
    public void setType(String type) {
        this.eventType = type;
    }

    /**
     * @return the year in which the event occurred
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year in which the event occurred
     */
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int hashCode() {
        return eventID.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        return eventID.equals(((Event) o).eventID);
    }

    @Override
    public int compareTo(Event o) {
        if (o == null) {
            return 1;
        }

        // birth events are always first

        if ("birth".equalsIgnoreCase(this.getType()) &&
                !"birth".equalsIgnoreCase(o.getType())) {
            return -1;
        }

        if (!"birth".equalsIgnoreCase(this.getType()) &&
                "birth".equalsIgnoreCase(o.getType())) {
            return 1;
        }

        // death events are always last

        if ("death".equalsIgnoreCase(this.getType()) &&
                !"death".equalsIgnoreCase(o.getType())) {
            return 1;
        }

        if (!"death".equalsIgnoreCase(this.getType()) &&
                "death".equalsIgnoreCase(o.getType())) {
            return -1;
        }

        // sort primarily by date, break ties by lower-case type

        if (this.getYear() == o.getYear()) {

            if (this.getType() == null && o.getType() == null) {
                return 0;
            }

            if (this.getType() == null && o.getType() != null) {
                return -1;
            }

            if (this.getType() != null && o.getType() == null) {
                return 1;
            }

            return this.getType().toLowerCase().compareTo(o.getType().toLowerCase());
        }

        return new Integer(this.getYear()).compareTo(o.getYear());
    }
}