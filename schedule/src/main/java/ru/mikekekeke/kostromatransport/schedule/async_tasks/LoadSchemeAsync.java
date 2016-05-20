package ru.mikekekeke.kostromatransport.schedule.async_tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import ru.mikekekeke.kostromatransport.schedule.exceptions.FileLoadException;
import ru.mikekekeke.kostromatransport.schedule.utils.Loader;

/**
 * Created by Mikekeke on 29-Mar-16.
 */
public final class LoadSchemeAsync extends AsyncTask<File, Void, Boolean> {
    private static final String TAG = LoadSchemeAsync.class.getSimpleName();

    private LoadSchemeListener mListener;
    private ProgressDialog mProgressDialog;

    public interface LoadSchemeListener {
        void onSchemeLoadSuccess();
        void onSchemeLoadFail();
    }

    public LoadSchemeAsync (LoadSchemeListener context) {
        mListener = context;
        mProgressDialog = new ProgressDialog((Context) context);
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.setTitle("Загрузка структуры");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    @Override
    protected Boolean doInBackground(File... fileArgs) {
        if (fileArgs.length != 1) {
            throw new IllegalArgumentException("Should be only one file to download. No more, no less.");
        }
        boolean fileLoaded;
        try {
            Loader.loadDataScheme(fileArgs[0]);
            fileLoaded = true;
        } catch (IOException | FileLoadException e) {
            fileLoaded = false;
            Log.e(TAG, "doInBackground: ", e);
        }
        return fileLoaded;
    }

    @Override
    protected void onPostExecute(final Boolean fileLoaded) {
        if (fileLoaded){
            mListener.onSchemeLoadSuccess();
        } else {
            mListener.onSchemeLoadFail();
        }
        mProgressDialog.dismiss();
    }
}
