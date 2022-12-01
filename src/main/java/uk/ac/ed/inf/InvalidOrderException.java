package uk.ac.ed.inf;

public class InvalidOrderException extends Exception{
    String message;
    InvalidOrderException(String str) {
        message = str;
    }
    public String toString() {
        return ("Invalid Order Exception Occurred : " + message);
    }
}

