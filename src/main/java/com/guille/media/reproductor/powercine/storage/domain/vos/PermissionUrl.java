package com.guille.media.reproductor.powercine.storage.domain.vos;

import java.util.Map;

public record PermissionUrl(String presignedUrl, String method, Map<String, String> headers) {

}
