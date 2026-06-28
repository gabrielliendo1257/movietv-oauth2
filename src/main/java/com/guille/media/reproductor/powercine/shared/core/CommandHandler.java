package com.guille.media.reproductor.powercine.shared.core;

public interface CommandHandler<C, R>
{
    R handler(C command);
}
