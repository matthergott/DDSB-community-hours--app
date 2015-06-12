package com.mhergott.ddsbcommunityhours;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Matt on 10/06/2015.
 */
public class SubmitDialog extends DialogFragment{
    public interface NoticeDialogListener {
        public void onSubmitDialogCapture(DialogFragment dialog);
        public void onSubmitDialogSelect(DialogFragment dialog);
        public void onSubmitDialogCancel(DialogFragment dialog);
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
                .setMessage("How do you want to add the photo?")
                .setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSubmitDialogCapture(SubmitDialog.this);
                    }
                })
                .setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSubmitDialogSelect(SubmitDialog.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSubmitDialogCancel(SubmitDialog.this);
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }


}
