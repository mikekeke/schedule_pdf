package ru.mikekekeke.kostromatransport.schedule.model;

import java.io.File;

/**
 * Created by Mikekeke on 04-Mar-16.
 */
public final class ScheduleItem {
    public static final String imgDirectory =
            DataScheme.appFolder
            + "img"
            + File.separatorChar;

    private String localImgPath;
    private String type;
    private int number;
    private String imageLink;
    private int date;

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public String getImageLink() {
        return imageLink;
    }

    @Override
    public String toString() {
        return type + " №" + number;
    }

    public String getLocalImgPath() {
        return imgDirectory + getType() + getNumber() + ".pdf";
    }

    public void setLocalImgPath(String localImgPath) {
        this.localImgPath = localImgPath;
    }
}
