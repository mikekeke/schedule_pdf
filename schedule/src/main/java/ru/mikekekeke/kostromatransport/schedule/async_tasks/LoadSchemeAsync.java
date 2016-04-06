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
public final class LoadSchemeAsync extends AsyncTask<Void, Void, File> {

    private LoadSchemeListener mListener;
    private ProgressDialog progressDialog;

    public interface LoadSchemeListener {
        void onSchemeLoadSuccess(File schemeFile);
        void onSchemeLoadFail();
    }

    public LoadSchemeAsync (LoadSchemeListener context) {
        mListener = context;
        progressDialog = new ProgressDialog((Context) context);
    }


    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Загрузка структуры");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected File doInBackground(Void... params) {
        File dataFile = new File("");
        try {
            dataFile = Loader.loadDataScheme();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileLoadException e) {
            e.printStackTrace();
        }
        return dataFile;
    }

    @Override
    protected void onPostExecute(final File dataFile) {
        if (dataFile.exists()){
            mListener.onSchemeLoadSuccess(dataFile);
        } else {
            mListener.onSchemeLoadFail();
        }
        progressDialog.dismiss();
    }
}
