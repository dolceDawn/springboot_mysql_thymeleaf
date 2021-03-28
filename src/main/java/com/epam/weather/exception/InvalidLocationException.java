package com.epam.weather.exception;

public class InvalidLocationException extends Exception {

    public InvalidLocationException() { super(); }

    public InvalidLocationException(String msg) { super(msg); }

    public InvalidLocationException(String msg, Throwable t) { super(msg, t); }
}
