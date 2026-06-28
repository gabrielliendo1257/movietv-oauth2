package com.guille.media.reproductor.powercine.storage.app.commands.requets;

import com.guille.media.reproductor.powercine.storage.domain.vos.MimeType;

public record CreateUploadCommand(
        String filename,
        long size,
        MimeType mimeType) {

}
