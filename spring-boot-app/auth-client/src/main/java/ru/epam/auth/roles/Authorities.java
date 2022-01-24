package ru.epam.auth.roles;

public class Authorities {

    private Authorities() {
        throw new UnsupportedOperationException("This is singleton class");
    }

    public static final String USER = "hasAuthority('user')";
}
