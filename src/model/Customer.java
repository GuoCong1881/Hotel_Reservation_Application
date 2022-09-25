package model;

import java.util.regex.Pattern;

public class Customer {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected static final String emailRegex = "^(.+)@(.+).com$";
    protected static final Pattern pattern = Pattern.compile(emailRegex);

    public Customer(String firstName, String lastName, String email){
        if (!pattern.matcher(email).matches()){
            throw new IllegalArgumentException("Error, invalid email address");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public static Pattern getPattern(){
        return pattern;
    }

    @Override
    public String toString(){
        return firstName + " " + lastName + " : " + email;
    }
}
