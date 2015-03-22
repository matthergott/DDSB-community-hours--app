package com.mhergott.ddsbcommunityhours;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivityFooter extends Fragment {

    private static TextView footerNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_footer, container, false);

        footerNumber = (TextView) view.findViewById(R.id.footerNumber);

        return view;
    }

    public void setHours (String str){
        footerNumber.setText(str);
    }
}
