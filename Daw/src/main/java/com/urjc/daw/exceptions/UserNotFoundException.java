package com.urjc.daw.exceptions;

public class UserNotFoundException extends RuntimeException {
    UserNotFoundException(String name) {
        super("Could not find user with name: " + name);
    }
}
