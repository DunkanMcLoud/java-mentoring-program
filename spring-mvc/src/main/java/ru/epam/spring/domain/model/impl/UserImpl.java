package ru.epam.spring.domain.model.impl;

import ru.epam.spring.domain.model.User;

public class UserImpl implements User {

    private static long ID_GEN = 0L;
    private long id;
    private String name;
    private String email;

    public UserImpl(String name, String email) {
        this.id = ID_GEN++;
        this.name = name;
        this.email = email;
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }
}
