package com.mhergott.ddsbcommunityhours;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class SelectLogTypeDialog extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogSingleClick(DialogFragment dialog);
        public void onDialogRecurringClick(DialogFragment dialog);
    }
    NoticeDialogListener mListener;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Event Type")
                .setItems(new String[] {"Single Time Event", "Recurring Event"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                            mListener.onDialogSingleClick(SelectLogTypeDialog.this);
                        if(which==1)
                            mListener.onDialogRecurringClick(SelectLogTypeDialog.this);
                    }
                });
        return builder.create();
    }


}
