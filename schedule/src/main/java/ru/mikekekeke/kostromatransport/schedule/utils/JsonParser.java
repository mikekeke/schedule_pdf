package ru.mikekekeke.kostromatransport.schedule.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ru.mikekekeke.kostromatransport.schedule.model.DataScheme;

/**
 * Created by Mikekeke on 28-Mar-16.
 */
public final class JsonParser {
    private File file;

    private JsonParser(final File file){
        this.file = file;
    }

    public static JsonParser getInstance(final File file){
        return new JsonParser(file);
    }

    public DataScheme parseBaseModel() throws IOException {
        return new Gson().fromJson(getJsonString(), DataScheme.class);
    }

    private String getJsonString() throws IOException {
        InputStream is = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
