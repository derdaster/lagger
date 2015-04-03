package com.android.lagger.model.entities;

/**
 * Created by Kubaa on 2015-04-03.
 */
public class Friend extends Entity {

    private String firstName;
    private String lastName;
    private String email;

    public Friend(int inID, String inFirstName, String inLastName, String inEmail) {
        super(inID);
        firstName = inFirstName;
        lastName = inLastName;
        email = inEmail;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
}