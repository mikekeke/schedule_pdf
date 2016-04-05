package ru.mikekekeke.kostromatransport.schedule.model;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Arrays;

import ru.mikekekeke.kostromatransport.schedule.Settings;

/**
 * Created by Mikekeke on 03-Mar-16.
 */
public class DataScheme {

    final public static String TAG = DataScheme.class.getSimpleName();
    final public static String appFolder =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separatorChar
                    + "Download"
                    + File.separatorChar
                    + "ru.mikekekeke.kostromatransport.schedule"
                    + File.separatorChar;


    final public static File schemeFile = new File(DataScheme.appFolder, Settings.JSON_FILE);

    int version;
    String[] types;
    ScheduleItem[] schedule_items;

    public void print(){
        Log.i(TAG, "version: " + version);
        Log.i(TAG, "types: " + Arrays.toString(types));
        Log.i(TAG, "items num: " + schedule_items.length);
    }

    public String[] getTypes() {
        return types;
    }

    public ScheduleItem[] getScheduleItems(){
        return schedule_items;
    }

}