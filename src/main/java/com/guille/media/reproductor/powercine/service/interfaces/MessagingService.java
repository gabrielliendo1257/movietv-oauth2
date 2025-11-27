package com.guille.media.reproductor.powercine.service.interfaces;

import com.guille.media.reproductor.powercine.dto.request.MediaDto;

public interface MessagingService
{
    void sendMovie(MediaDto media);
}
