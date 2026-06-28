package com.guille.media.reproductor.powercine.user.domain.vos;

import com.guille.media.reproductor.powercine.user.domain.exceptions.CustomerException;

public record Username (
        String username
) implements BaseProperty
{
    public Username {
        if (this.username().length() < MIN_LENGTH || this.username().length() > MAX_LENGTH) {
            throw new CustomerException("Usuario ya existente.");
        }
    }
}
