package ru.mikekekeke.kostromatransport.schedule.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import ru.mikekekeke.kostromatransport.schedule.StartActivity;
import ru.mikekekeke.kostromatransport.schedule.R;

/**
 * Created by Mikekeke on 04-Mar-16.
 */
public class LoadDialog extends android.support.v4.app.DialogFragment {

    public static LoadDialog newInstance(int title) {
        LoadDialog frag = new LoadDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
//                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_load,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((StartActivity)getActivity()).loadDialogLoadClick();
                                dismiss();
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((StartActivity)getActivity()).loadDialogCancelClick();
                                dismiss();
                            }
                        }
                )
                .create();

    }
}
