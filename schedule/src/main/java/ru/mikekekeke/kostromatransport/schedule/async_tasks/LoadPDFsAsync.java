package ru.mikekekeke.kostromatransport.schedule.async_tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ru.mikekekeke.kostromatransport.schedule.R;
import ru.mikekekeke.kostromatransport.schedule.Settings;
import ru.mikekekeke.kostromatransport.schedule.exceptions.FileLoadException;
import ru.mikekekeke.kostromatransport.schedule.model.ScheduleItem;
import ru.mikekekeke.kostromatransport.schedule.utils.Loader;

/**
 * Created by Mikekeke on 29-Mar-16.
 */
public final class LoadPDFsAsync extends AsyncTask<Void, Integer, Void> {
    private static final String TAG = LoadPDFsAsync.class.getSimpleName();

    //todo move loader to service

    private LoadPDFsListener mListener;
    private Context context;
    List<ScheduleItem> noImgItems;
    ProgressDialog pDial;

    public interface LoadPDFsListener {
        void onPDFsLoadFinish();
    }


    public LoadPDFsAsync (final LoadPDFsListener context, final List<ScheduleItem> noImgItems){
        mListener = context;
        this.context = (Context) context;
        this.noImgItems = noImgItems;
        pDial = new ProgressDialog(this.context);
    }

    @Override
    protected void onPreExecute() {
        pDial.setMax(noImgItems.size());
        pDial.setTitle(R.string.loading_images);
        pDial.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDial.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i = 0; i< noImgItems.size(); i++) {
            ScheduleItem item = noImgItems.get(i);

            try {
                Loader.loadFile(
                        item.getImageLink(),
                        new File(context.getFilesDir(),
                                item.getName() + Settings.FILE_EXT)
                );
                publishProgress(i + 1);
            } catch (IOException | FileLoadException e) {
                Log.e(TAG, "doInBackground: ", e);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        pDial.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        pDial.dismiss();
        mListener.onPDFsLoadFinish();
    }
}
