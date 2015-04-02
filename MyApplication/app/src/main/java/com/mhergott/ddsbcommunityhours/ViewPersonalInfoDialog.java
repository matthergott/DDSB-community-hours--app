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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ViewPersonalInfoDialog extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogEditInfo(DialogFragment dialog);
        public void onDialogCancel(DialogFragment dialog);
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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_personal_info_dialog, null);

        String personalInformation = "";
        FileInputStream fis2;
        try {
            fis2 = getActivity().openFileInput("personalInfo.txt");
            byte[] input = new byte[fis2.available()];
            while (fis2.read(input) != -1) {}
            personalInformation = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String name = personalInformation.substring(0, personalInformation.indexOf(";"));
        personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
        String school = personalInformation.substring(0, personalInformation.indexOf(";"));
        personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
        String day = personalInformation.substring(0, personalInformation.indexOf(";"));
        personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
        String month = personalInformation.substring(0, personalInformation.indexOf(";"));
        personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
        String year = personalInformation.substring(0, personalInformation.indexOf(";"));

        TextView nameView = (TextView) view.findViewById(R.id.name_personal_dialog);
        nameView.setText(name);
        TextView schoolView = (TextView) view.findViewById(R.id.school_personal_dialog);
        schoolView.setText(school);
        TextView dateView = (TextView) view.findViewById(R.id.dob_personal_dialog);
        dateView.setText(day + "/" + month + "/" + year);

        builder.setMessage("Personal Information")
                .setPositiveButton("Edit Information", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogEditInfo(ViewPersonalInfoDialog.this);                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogCancel(ViewPersonalInfoDialog.this);                      }
                });

        builder.setView(view);
        return builder.create();
    }
}
