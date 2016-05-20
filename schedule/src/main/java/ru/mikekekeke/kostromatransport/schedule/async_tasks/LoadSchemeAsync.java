package ru.mikekekeke.kostromatransport.schedule.async_tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;

import ru.mikekekeke.kostromatransport.schedule.exceptions.FileLoadException;
import ru.mikekekeke.kostromatransport.schedule.utils.Loader;

/**
 * Created by Mikekeke on 29-Mar-16.
 */
public final class LoadSchemeAsync extends AsyncTask<File, Void, Boolean> {

    private LoadSchemeListener mListener;
    private ProgressDialog mprogressDialogP;

    public interface LoadSchemeListener {
        void onSchemeLoadSuccess();
        void onSchemeLoadFail();
    }

    public LoadSchemeAsync (LoadSchemeListener context) {
        mListener = context;
        mprogressDialogP = new ProgressDialog((Context) context);
    }


    @Override
    protected void onPreExecute() {
        mprogressDialogP.setTitle("Загрузка структуры");
        mprogressDialogP.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mprogressDialogP.setIndeterminate(true);
        mprogressDialogP.show();
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
            e.printStackTrace();
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
        mprogressDialogP.dismiss();
    }
}
