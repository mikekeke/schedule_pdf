package ru.mikekekeke.kostromatransport.schedule.utils;

import ru.mikekekeke.kostromatransport.schedule.Settings;
import ru.mikekekeke.kostromatransport.schedule.exceptions.FileLoadException;
import ru.mikekekeke.kostromatransport.schedule.model.DataScheme;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mikekeke on 03-Mar-16.
 */
public class Loader {
    public static final String RES_OK = "Ok";
    public static final String RES_ERR = "Error";


    public static void loadPDF(){

    }

    public static File loadDataScheme() throws IOException, FileLoadException {
        File jsonFile = loadFile(Settings.JSON_LINK,
                new File(DataScheme.appFolder, Settings.JSON_FILE));
        if (!jsonFile.exists()) {
            throw new FileLoadException("JSON scheme file does not exists!");
        }

        return jsonFile;
    }

    public static File loadFile(final String mUrl, final File file) throws IOException, FileLoadException {
//        File jsonFile = new File(DataScheme.appFolder,fileName );
        HttpURLConnection conn = getConnection(mUrl);
        final InputStream input = conn.getInputStream();
        final OutputStream output = new FileOutputStream(file);

        byte data[] = new byte[4096];
        int count;
        while ((count = input.read(data)) != -1) {
            // allow canceling with back button
//            if (isCancelled()) {
//                input.close();
//                return null;
//            }
//            total += count;
            // publishing the progress....
//            if (fileLength > 0) // only if total length is known
//                publishProgress((int) (total * 100 / fileLength));
            output.write(data, 0, count);
        }
        return file;
    }

    private static HttpURLConnection getConnection(final String mUrl) throws IOException, FileLoadException {
        URL url = new URL(mUrl);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        int resp = connection.getResponseCode();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
            throw new FileLoadException("Server returned HTTP " + connection.getResponseCode()
                    + " " + connection.getResponseMessage());
        } else return connection;
    }
}
