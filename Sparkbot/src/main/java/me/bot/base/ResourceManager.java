package me.bot.base;

import java.io.File;

public class ResourceManager {

    private String BASE_FOLDER;

    private File
            languages,
            configs;

    public ResourceManager(String folder) {
        this.BASE_FOLDER = folder;
        languages = new File(BASE_FOLDER + "langs");
        configs = new File(BASE_FOLDER + "configs");
    }



}
