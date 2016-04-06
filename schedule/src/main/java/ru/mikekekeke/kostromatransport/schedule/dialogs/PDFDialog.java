package ru.mikekekeke.kostromatransport.schedule.dialogs;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;

import java.io.File;

import ru.mikekekeke.kostromatransport.schedule.R;
import ru.mikekekeke.kostromatransport.schedule.model.ScheduleItem;

/**
 * Created by Mikekeke on 29-Mar-16.
 */
public class PDFDialog extends DialogFragment{

    private static final String FILE = "FILE";
    private static final String TITLE = "TITLE";

    private PDFView pdfView;
    private TextView title, closeTv;
    private float initOffsetX, initOffsetY;

    public static PDFDialog creteInstance(final ScheduleItem item) {
        PDFDialog dialog = new PDFDialog();
        dialog.setCancelable(true);
        Bundle args = new Bundle();
        args.putString(FILE, item.getLocalImgPath());
        args.putString(TITLE, item.toString());
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.FullscreenDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pdf_dialog, container, false);
        pdfView = (PDFView) v.findViewById(R.id.pdfView);
        title = (TextView) v.findViewById(R.id.pdfTitleTv);
        title.setText(getArguments().getString(TITLE));
        closeTv = (TextView) v.findViewById(R.id.closeTv);
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDFDialog.this.dismiss();
            }
        });

        /**
        pdfView.zoomTo(2.0f);
        pdfView.loadPages();
         */

        loadPDF();
        return v;
    }

    private void loadPDF() {
        Bundle args = getArguments();
        Log.d("_tt", args.toString());
        File pdfFile = new File(args.getString(FILE));
        pdfView.fromFile(pdfFile)
                .showMinimap(false)
                .enableSwipe(true)
                .load();
    }


}
