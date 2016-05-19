package ru.mikekekeke.kostromatransport.schedule.model;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import ru.mikekekeke.kostromatransport.schedule.Settings;

/**
 * Created by Mikekeke on 03-Mar-16.
 */
public final class DataScheme {

    final public static String TAG = DataScheme.class.getSimpleName();
    final public static String APP_FOLDER =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separatorChar
                    + "Android"
                    + File.separatorChar
                    + "data"
                    + File.separatorChar
                    + "ru.mikekekeke.kostromatransport.schedule"
                    + File.separatorChar;

//                    + File.separatorChar
//                    + "KosTransport"
//                    + File.separatorChar;




//    final public static File SCHEME_FILE = new File(DataScheme.APP_FOLDER, Settings.JSON_FILE);
    final public static String SCHEME_FILE = Settings.JSON_FILE;

    private int version;
    private Type[] types;
    private ScheduleItem[] schedule_items;

    public void print(){
        Log.i(TAG, "version: " + version);
//        Log.i(TAG, "types: " + Arrays.toString(types));
        Log.i(TAG, "items num: " + schedule_items.length);
    }

    public Type[] getTypes() {
        return types;
    }

    public ScheduleItem[] getScheduleItems(){
        return schedule_items;
    }

}
