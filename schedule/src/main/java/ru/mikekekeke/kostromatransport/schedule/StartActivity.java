package ru.mikekekeke.kostromatransport.schedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.mikekekeke.kostromatransport.schedule.async_tasks.LoadPDFsAsync;
import ru.mikekekeke.kostromatransport.schedule.async_tasks.LoadSchemeAsync;
import ru.mikekekeke.kostromatransport.schedule.dialogs.LoadDialog;
import ru.mikekekeke.kostromatransport.schedule.exceptions.FileLoadException;
import ru.mikekekeke.kostromatransport.schedule.model.DataScheme;
import ru.mikekekeke.kostromatransport.schedule.model.ScheduleItem;
import ru.mikekekeke.kostromatransport.schedule.utils.JsonParser;
import ru.mikekekeke.kostromatransport.schedule.utils.Loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity
                                implements LoadSchemeAsync.LoadSchemeListener,
                                            LoadPDFsAsync.LoadPDFsListener{

    public static final String PDF_URL = "ru.mikekekeke.kostromatransport.PDF_URL";
    public static final String RELOAD = "ru.mikekekeke.kostromatransport.RELOAD";
    Button loadBtn;
    TextView feedbackTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File appFolder = new File(DataScheme.appFolder);
        if (!appFolder.exists()) appFolder.mkdir();
        File imgFolder = new File(ScheduleItem.imgDirectory);
        if (!imgFolder.exists()) imgFolder.mkdir();

        feedbackTxt = (TextView) findViewById(R.id.mainTxt);
        loadBtn = (Button) findViewById(R.id.buttonLoad);
        boolean reload = getIntent().getBooleanExtra(RELOAD, false);
        initialize(reload);
        }

    private void initialize(boolean reload) {
        File fileScheme = DataScheme.schemeFile;
        if (fileScheme.exists() && !reload){
            try {
                checkImageLoadStatus(fileScheme);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LoadDialog.newInstance(R.string.need_load_files).show(getSupportFragmentManager(), "dialog");
//            LoadDialog.newInstance(R.string.need_load_files).show(getFragmentManager(), "dialog");
        }
    }

    public void loadDialogLoadClick() {
        new LoadSchemeAsync(this).execute();
    }

    @Override
    public void onSchemeLoadSuccess(File schemeFile) {
        try {
            checkImageLoadStatus(schemeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSchemeLoadFail() {

    }

    @Override
    public void onPDFsLoadFinish() {
        showSchedule();
    }

    private void checkImageLoadStatus(final File fileScheme) throws IOException {
        DataScheme scheme = JsonParser.getInstance(fileScheme).parseBaseModel();
        List<ScheduleItem> noImgItems = checkImages(scheme);
        if (noImgItems.size() > 0){
            new LoadPDFsAsync(this, noImgItems).execute();
        } else {
            showSchedule();
        }
    }

    /**
     * check if all items have images
     * @param scheme
     * @return List of schedule items w/o images
     */
    private List<ScheduleItem> checkImages(final DataScheme scheme) {
        List<ScheduleItem> noImgItems = new ArrayList<ScheduleItem>();
        for (ScheduleItem item : scheme.getScheduleItems()){
            String path = item.getLocalImgPath();
            if (!new File(path).exists()) {
                noImgItems.add(item);
            }
        }
        return noImgItems;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSchedule() {
        File dataScheme = new File(DataScheme.appFolder, Settings.JSON_FILE);
        if (!dataScheme.exists()){
            //todo report file not exists
            return;
        }
        startActivity(new Intent(this, ScheduleListActivity.class));
    }

    //CLICKS
    public void loadDialogCancelClick() {
        loadBtn.setVisibility(View.VISIBLE);
    }

    public void startLoading(View view) {
        initialize(true);
    }



}
