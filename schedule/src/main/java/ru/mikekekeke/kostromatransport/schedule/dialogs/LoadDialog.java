package ru.mikekekeke.kostromatransport.schedule.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import ru.mikekekeke.kostromatransport.schedule.R;
import ru.mikekekeke.kostromatransport.schedule.ScheduleListActivity;

/**
 * Created by Mikekeke on 04-Mar-16.
 */
public class LoadDialog extends android.support.v4.app.DialogFragment {
    private static final String TITLE = "LoadDialog.TITLE";
    private static final String RELOAD = "LoadDialog.RELOAD";

    private LoadDialogListener mListener;

    public interface LoadDialogListener {
        void onLoadDialogLoadClick();
        void onLoadDialogCancelClick();
    }

    public static LoadDialog newInstance(LoadDialogListener listener, final int title, final boolean reload) {
        LoadDialog frag = new LoadDialog();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        args.putBoolean(RELOAD, reload);
        frag.setArguments(args);
        return frag;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(TITLE);
        final boolean reload = getArguments().getBoolean(RELOAD);

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (LoadDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
            + "must implement LoadDialogListener");
        }
    }
}
