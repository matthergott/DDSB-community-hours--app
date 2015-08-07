package com.mhergott.ddsbcommunityhours;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmAddSignatureDialog extends DialogFragment {
    public interface NoticeDialogListener {
        public void onSignatureDialogCapture(DialogFragment dialog);
        public void onSignatureDialogSelect(DialogFragment dialog);
        public void onSignatureDialogLater(DialogFragment dialog);
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
        builder.setTitle("Add a signature photo to event")
                .setMessage("Where is your photo?")
                .setPositiveButton("Skip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSignatureDialogLater(ConfirmAddSignatureDialog.this);
                    }
                })
                .setNeutralButton("Take New", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSignatureDialogCapture(ConfirmAddSignatureDialog.this);
                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSignatureDialogSelect(ConfirmAddSignatureDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
