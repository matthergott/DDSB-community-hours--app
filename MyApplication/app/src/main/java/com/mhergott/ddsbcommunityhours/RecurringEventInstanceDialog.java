package com.mhergott.ddsbcommunityhours;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class RecurringEventInstanceDialog extends DialogFragment {

    NoticeDialogListener mListener;
    String date;
    String hours;

    public interface NoticeDialogListener {
        public void onDialogDelete(DialogFragment dialog);
        public void onDialogCancel(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.recurring_event_instance_dialog, null);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        date = sharedPref.getString("recurring_event_instance_date", "");
        hours = sharedPref.getString("recurring_event_instance_hours", "");

        TextView dateView = (TextView) view.findViewById(R.id.recurringEventInstanceDialogDateText);
        dateView.setText(date);
        TextView hoursView = (TextView) view.findViewById(R.id.recurringEventInstanceDialogHoursText);
        hoursView.setText(hours);

        builder.setMessage("Event Instance Info")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogDelete(RecurringEventInstanceDialog.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogCancel(RecurringEventInstanceDialog.this);
                    }
                });

        builder.setView(view);
        return builder.create();
    }

}
