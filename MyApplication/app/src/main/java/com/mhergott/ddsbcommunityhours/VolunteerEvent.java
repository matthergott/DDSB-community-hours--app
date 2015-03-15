package com.mhergott.ddsbcommunityhours;


import android.support.v7.app.ActionBarActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private String name;
    private String description;
    private String organisation;
    private String hours;
    private String str;
    //pictures??

    public VolunteerEvent(String n) {
        name = n;

        FileInputStream fis;
        try {
            fis = openFileInput(name + ".txt");
            byte[] input = new byte[fis.available()];
            while (fis.read(input) != -1) {
            }
            str = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        description = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        organisation = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        hours = str.substring(0, str.indexOf(';'));
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

