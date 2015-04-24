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
    private boolean recurring;
    ArrayList<String> hoursList = new ArrayList<>();
    //pictures??

    public VolunteerEvent(String str) {
        name = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        description = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        organisation = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        hours = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
    }

    public VolunteerEvent(String str,int i) {
        name = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        description = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        organisation = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        hours = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        if(i == 0)
            recurring = true;
        else
            recurring = false;
        if(recurring){
            while(str.indexOf(';')!=-1){
                String toAdd = str.substring(0, str.indexOf(';')+1);
                str = str.substring(str.indexOf(';')+1);
                toAdd = toAdd + str.substring(0, str.indexOf(';')+1);
                str = str.substring(str.indexOf(';') + 1);
                //day month year;hours;
                Log.i("MyActivity", toAdd);
                hoursList.add(toAdd);
            }
        }
        else
            hoursList = null;
    }

    public void saveData(){
        String toFile = name + ";" +  description + ";" + organisation + ";" + hours + ";";
        if(hoursList!=null){
            for (int a = 0; a < hoursList.size(); a++){
                toFile = toFile + hoursList.get(a).toString();
            }
        }
        String fileName = name + ".txt";
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(toFile.getBytes());
            fos.close();
        } catch (Exception e) {}
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
    public String addHours(String string){
        Log.i("MyActivity", string);
        Log.i("MyActivity", name);
        hoursList.add(string.toString());
        string = string.substring(string.indexOf(';') + 1);
        int temp = Integer.valueOf(hours);
        temp += Integer.valueOf(string.substring(0,string.indexOf(';')));
        hours = String.valueOf(temp);

        Log.i("MyActivity", hours);
        Log.i("MyActivity", hoursList.get(0));

        String toFile = name + ";" +  description + ";" + organisation + ";" + hours + ";";
        if(hoursList!=null){
            for (int a = 0; a < hoursList.size(); a++){
                toFile = toFile + hoursList.get(a).toString();
            }
        }
        String fileName = name + ".txt";
        Log.i("MyActivity", fileName);
        Log.i("MyActivity", toFile);
        /*
            try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(toFile.getBytes());
            fos.close();
        } catch (Exception e) {}
        */
        return toFile;
    }
    public ArrayList<String> getHoursList(){
        return hoursList;
    }
}

