package com.techelevator.tenmo.exception;

public class InsufficientFundsException extends  Exception{
    public InsufficientFundsException() {
        super("Insufficient funds");
    }
}
