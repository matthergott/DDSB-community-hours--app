package com.mhergott.ddsbcommunityhours;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfirmDeleteDialog extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogDeleteConfirm(DialogFragment dialog);
        public void onDialogCancelConfirm(DialogFragment dialog);
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
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Event Instance?")
                .setMessage("The instance will be permanently deleted")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogDeleteConfirm(ConfirmDeleteDialog.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogCancelConfirm(ConfirmDeleteDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
