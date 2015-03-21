package com.mhergott.ddsbcommunityhours;


import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class VolunteerEvent extends ActionBarActivity {
    private static final String TAG = "mattsMessage";
    private String name;
    private String description;
    private String organisation;
    private String hours;
    //pictures??

    public VolunteerEvent(String str) {
        /*
        //////////////////////////////////////////////////////
        FileInputStream fis;
        String fileName = name + ".txt";
        Log.i(TAG, fileName);
        try {
            fis = openFileInput(fileName);
            byte[] input = new byte[fis.available()];
            while (fis.read(input) != -1) {
            }
            str = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////
        */


        //str = "swimming;swam at rec complex;City of Pickering;14;";
        /////////////////////////////////////////////////////

        name = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        description = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        organisation = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        hours = str.substring(0, str.indexOf(';'));
        /////////////////////////////////////////////////
    }

    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getOrganisation(){
        return organisation;
    }
    public String getHours(){
        return hours;
    }
}

