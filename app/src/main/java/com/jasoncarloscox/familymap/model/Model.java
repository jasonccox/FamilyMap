package com.jasoncarloscox.familymap.model;

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
    private User user;

    private Model() {}

    public FamilyTree getTree() {
        return tree;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
