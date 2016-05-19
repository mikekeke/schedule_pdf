package ru.mikekekeke.kostromatransport.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mikekekeke.kostromatransport.schedule.async_tasks.LoadPDFsAsync;
import ru.mikekekeke.kostromatransport.schedule.async_tasks.LoadSchemeAsync;
import ru.mikekekeke.kostromatransport.schedule.dialogs.LoadDialog;
import ru.mikekekeke.kostromatransport.schedule.model.DataScheme;
import ru.mikekekeke.kostromatransport.schedule.model.ScheduleItem;
import ru.mikekekeke.kostromatransport.schedule.utils.JsonParser;

public class StartActivity extends AppCompatActivity
                                implements LoadSchemeAsync.LoadSchemeListener,
                                            LoadPDFsAsync.LoadPDFsListener,
        LoadDialog.LoadDialogListener{

    public static final String RELOAD = "ru.mikekekeke.kostromatransport.RELOAD";
    Button loadBtn;
    TextView feedbackTxt;
    private boolean reload;
    private File mSchemeFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File appFolder = new File(DataScheme.APP_FOLDER);
        if (!appFolder.exists()) appFolder.mkdir();
        File imgFolder = new File(ScheduleItem.imgDirectory);
        if (!imgFolder.exists()) imgFolder.mkdir();

        feedbackTxt = (TextView) findViewById(R.id.infoTxt);
        loadBtn = (Button) findViewById(R.id.buttonLoad);
        reload = getIntent().getBooleanExtra(RELOAD, false);
        mSchemeFile = getFileStreamPath(DataScheme.SCHEME_FILE);
        initialize();
        }

    private void initialize() {
        if (mSchemeFile.exists() && !reload){
            try {
                checkImageLoadStatus(mSchemeFile, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LoadDialog.newInstance(reload).show(getSupportFragmentManager(), "dialog");
//            LoadDialog.newInstance(R.string.need_load_files).show(getFragmentManager(), "dialog");
        }
    }

    @Override
    public void onSchemeLoadSuccess() {
        try {
            checkImageLoadStatus(mSchemeFile, reload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSchemeLoadFail() {
        feedbackTxt.setText(R.string.err_scheme_loading);
        loadBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPDFsLoadFinish() {
        showSchedule();
    }

    private void checkImageLoadStatus(final File fileScheme, boolean reload) throws IOException {
        DataScheme scheme = JsonParser.getInstance(fileScheme).parseBaseModel();
        List<ScheduleItem> noImgItems = checkImages(scheme);
        if (reload) {
            new LoadPDFsAsync(this, Arrays.asList(scheme.getScheduleItems())).execute();
        } else if (noImgItems.size() > 0){
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
        if (mSchemeFile.exists()){
            //todo report file not exists
            return;
        }
        startActivity(new Intent(this, ScheduleListActivity.class));
    }

    //DIALOG CLICKS
    @Override
    public void onLoadDialogLoadClick() {
        new LoadSchemeAsync(this).execute(mSchemeFile);
    }
    @Override
    public void onLoadDialogCancelClick() {
        loadBtn.setVisibility(View.VISIBLE);
    }

    //ACTIVITY CLICKS
    public void startLoading(View view) {
        initialize();
    }



}
