package ru.mikekekeke.kostromatransport.schedule.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import ru.mikekekeke.kostromatransport.schedule.ScheduleListActivity;
import ru.mikekekeke.kostromatransport.schedule.StartActivity;
import ru.mikekekeke.kostromatransport.schedule.R;

/**
 * Created by Mikekeke on 04-Mar-16.
 */
public class LoadDialog extends android.support.v4.app.DialogFragment {

    private LoadDialogListener mListener;

    public interface LoadDialogListener {
        void onLoadDialogLoadClick();
        void onLoadDialogCancelClick();
    }

    public static LoadDialog newInstance(LoadDialogListener listener, final int title, final boolean reload) {
        LoadDialog frag = new LoadDialog();
        frag.setListener(listener);
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putBoolean("reload", reload);
        frag.setArguments(args);
        return frag;
    }

    private void setListener(LoadDialogListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        final boolean reload = getArguments().getBoolean("reload");

        return new AlertDialog.Builder(getActivity())
//                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setPositiveButton(reload ? R.string.alert_dialog_reload : R.string.alert_dialog_load,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mListener.onLoadDialogLoadClick();
                                dismiss();
                            }
                        }
                )
                .setNegativeButton(reload ? R.string.back_to_chedule : R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (reload){
                                    getActivity().startActivity(new Intent(getActivity(), ScheduleListActivity.class));
                                } else {
                                    mListener.onLoadDialogCancelClick();
                                }
                                dismiss();
                            }
                        }
                )
                .create();

    }
}
