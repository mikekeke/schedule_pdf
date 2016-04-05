package ru.mikekekeke.kostromatransport.schedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.mikekekeke.kostromatransport.schedule.dialogs.PDFDialog;
import ru.mikekekeke.kostromatransport.schedule.model.DataScheme;
import ru.mikekekeke.kostromatransport.schedule.model.ScheduleItem;
import ru.mikekekeke.kostromatransport.schedule.utils.JsonParser;

public class ScheduleListActivity extends AppCompatActivity {
    private LinearLayout rootList;
    public static final String TAG = ScheduleListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!DataScheme.schemeFile.exists()){
            startActivity(new Intent(this, StartActivity.class));
            finish();
        } else {
            init();
        }
    }

    private void init() {
        setContentView(R.layout.activity_schedule_list);
        rootList = (LinearLayout) findViewById(R.id.rootList);
        try {
            DataScheme model =
                    JsonParser.getInstance(DataScheme.schemeFile).parseBaseModel();
            populateList(model);
            model.print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateList(final DataScheme model) {
        String[] types = model.getTypes();
        ScheduleItem[] items = model.getScheduleItems();
        for (String type : types) {
            TextView tv = new TextView(this);
            tv.setText(type);
            rootList.addView(tv);
            addItemsInList(type, items);
        }
    }

    private void addItemsInList(final String type, final ScheduleItem[] items) {
        List<ScheduleItem> toAddList = new ArrayList<ScheduleItem>();
        for (ScheduleItem item : items) {
            if (item.getType().equals(type)){
                toAddList.add(item);
            }
        }
        ListView itemsListView =
                (ListView) getLayoutInflater()
                        .inflate(R.layout.schedule_list_view, null);
        ArrayAdapter<ScheduleItem> adapter
                = new ArrayAdapter<ScheduleItem>(this, R.layout.item_schedule, toAddList);
        itemsListView.setAdapter(adapter);
        itemsListView.setClickable(true);
        itemsListView.setOnItemClickListener(new ScheduleItemClickListener());
        rootList.addView(itemsListView);
    }

    private final class ScheduleItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ScheduleItem item =
                    (ScheduleItem) parent.getAdapter().getItem(position);
            showPDFDialog(item);
        }

        private void showPDFDialog(final ScheduleItem item) {
            PDFDialog dialog = PDFDialog.creteInstance(item);
            dialog.show(getSupportFragmentManager(), "TAG");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_reload:
                startReload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startReload() {
        Intent reload = new Intent(this, StartActivity.class);
        reload.putExtra(StartActivity.RELOAD, true);
        startActivity(reload);
    }
}
