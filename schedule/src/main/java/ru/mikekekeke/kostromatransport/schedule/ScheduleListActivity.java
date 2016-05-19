package ru.mikekekeke.kostromatransport.schedule;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.mikekekeke.kostromatransport.schedule.dialogs.PDFDialog;
import ru.mikekekeke.kostromatransport.schedule.model.DataScheme;
import ru.mikekekeke.kostromatransport.schedule.model.ScheduleItem;
import ru.mikekekeke.kostromatransport.schedule.model.Type;
import ru.mikekekeke.kostromatransport.schedule.utils.JsonParser;

public class ScheduleListActivity extends AppCompatActivity {
    private TabHost tabHost;
    public static final String TAG = ScheduleListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getFileStreamPath(DataScheme.SCHEME_FILE).exists()){
            startActivity(new Intent(this, StartActivity.class));
            finish();
        } else {
            init();
        }
    }

    private void init() {
        setContentView(R.layout.activity_schedule_list);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        try {
            DataScheme model =
                    JsonParser.getInstance(getFileStreamPath(DataScheme.SCHEME_FILE)).parseBaseModel();
            populateList(model);
            model.print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateList(final DataScheme model) {
        Type[] types = model.getTypes();
        for (final Type type : types) {
            TabHost.TabSpec spec = tabHost.newTabSpec(type.getName());
            spec.setIndicator(type.getName());
            spec.setContent(composeContent(type, model.getScheduleItems()));
            tabHost.addTab(spec);
        }

    }

    private TabHost.TabContentFactory composeContent(
            final Type type, final ScheduleItem[] items) {
        return new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                ListView listView = (ListView) getLayoutInflater().inflate(R.layout.schedule_list_view, null);
                ArrayAdapter<ScheduleItem> adapter = new ArrayAdapter<ScheduleItem>(
                        ScheduleListActivity.this,
                        R.layout.item_schedule,
                        pickItems(items, type.getId())
                );
                listView.setClickable(true);
                listView.setOnItemClickListener(new ScheduleItemClickListener());
                listView.setAdapter(adapter);
                return listView;
            }
        };
    }

    private List<ScheduleItem> pickItems(ScheduleItem[] items, int typeId) {
        List<ScheduleItem> pickedItems = new ArrayList<ScheduleItem>();
        for (ScheduleItem item : items) {
            if (item.getType() == typeId){
                pickedItems.add(item);
            }
        }
        return pickedItems;
    }

    private final class ScheduleItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ScheduleItem item =
                    (ScheduleItem) parent.getAdapter().getItem(position);
            if (Build.VERSION.SDK_INT >= 11) {
                showPDFDialog(item);
            } else {
                openInExternalViewer(item);
            }
        }
    }

    private void showPDFDialog(final ScheduleItem item) {
        PDFDialog dialog = PDFDialog.creteInstance(item);
        dialog.show(getSupportFragmentManager(), TAG);
    }

    private void openInExternalViewer(final ScheduleItem item) {
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType
                (Uri.fromFile(new File(item.getLocalImgPath())),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.need_to_install_PDF_viewer, Toast.LENGTH_LONG).show();
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
