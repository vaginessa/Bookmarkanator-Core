package com.bookmarking.exception;

public class ValueWithoutKeyException extends Exception
{
    public ValueWithoutKeyException()
    {
        super();
    }

    public ValueWithoutKeyException(String message)
    {
        super(message);
    }
}
