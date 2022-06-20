package com.techelevator.tenmo.exception;

public class InvalidTransferIdException extends Exception {
    public InvalidTransferIdException() {
        super("Please enter a valid transfer id.");
    }
}
