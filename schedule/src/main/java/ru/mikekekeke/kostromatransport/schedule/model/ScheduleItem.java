package ru.mikekekeke.kostromatransport.schedule.model;

import java.io.File;

import ru.mikekekeke.kostromatransport.schedule.Settings;

/**
 * Created by Mikekeke on 04-Mar-16.
 */
public final class ScheduleItem {
    public static final String imgDirectory =
            DataScheme.APP_FOLDER
            + "img"
            + File.separatorChar;

    private String localImgPath;
    private int type;
    private String name;
    private String imageLink;
    private int date;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }

    @Override
    public String toString() {

        return name;
    }

    public String getLocalImgPath() {
        return imgDirectory + getName() + Settings.FILE_EXT;
    }

}
