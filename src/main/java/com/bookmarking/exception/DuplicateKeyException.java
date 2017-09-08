package com.bookmarking.exception;

public class DuplicateKeyException extends Exception
{
    public DuplicateKeyException()
    {
        super();
    }

    public DuplicateKeyException(String message)
    {
        super(message);
    }
}
