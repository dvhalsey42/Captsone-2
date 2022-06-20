package com.techelevator.tenmo.exception;

public class InvalidUserException extends Exception {
    public InvalidUserException() {
        super("Please enter a valid user ID.");
    }
}
