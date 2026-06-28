package com.guille.media.reproductor.powercine.storage.app.errors;

public class InvalidConversion extends RuntimeException
{
    public InvalidConversion(String message, Exception cause)
    {
        super(message, cause);
    }
}
