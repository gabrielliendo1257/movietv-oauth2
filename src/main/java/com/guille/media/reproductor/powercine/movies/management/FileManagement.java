package com.guille.media.reproductor.powercine.movies.management;

import java.io.File;
import java.nio.file.Path;

public class FileManagement {

    private Path path;

    public FileManagement(Path path) {
        this.path = path;
    }

    public Boolean isFile(File file) {
        return file.isFile() && !file.isDirectory();
    }

    public String getUri(String baseHost) {
        return baseHost + this.getTitle();
    }

    public String getTitle() {
        return this.path.getFileName().toString();
    }

    public String getDescription() {
        return "Description Movie";
    }

    public Long getDuration() {
        return new File(this.path.toString()).length();
    }
}
