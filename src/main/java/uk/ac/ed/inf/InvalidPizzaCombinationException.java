package uk.ac.ed.inf;

public class InvalidPizzaCombinationException extends Exception{
    InvalidPizzaCombinationException() {
    }
    public String toString() {
        return ("Invalid Pizza Combination Exception Occurred");
    }
}
