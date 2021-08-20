package com.sg.guess_the_number.service;

/**
 * An exception thrown when a guess' format does not conform
 * to the service layer's standards
 */
public class InvalidGuessException extends Exception {
    /**
     * Constructs a new exception in the format
     * "Invalid guess "GUESS""
     * @param guess The guess that caused the exception
     */
    public InvalidGuessException(String guess) {
        super(String.format("Invalid guess \"%s\"", guess));
    }
}
