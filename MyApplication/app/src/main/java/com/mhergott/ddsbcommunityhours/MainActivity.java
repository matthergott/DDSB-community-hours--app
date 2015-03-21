package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.content.Intent;
import android.view.View;
import android.app.Activity;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "mattsMessage";
    List namesList = new ArrayList();
    String names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Read file in Internal Storage
        FileInputStream fis;
        try {
            fis = openFileInput("names.txt");
            byte[] input = new byte[fis.available()];
            while (fis.read(input) != -1) {}
            names = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(names!=null)
            arrCreator(names);


        String[] namesArr = new String[namesList.size()];
        namesList.toArray(namesArr);

        ArrayList<VolunteerEvent> eventList = new ArrayList<VolunteerEvent>();
        for(int j = 0; j < namesArr.length; j++){
            //Read file in Internal Storage
            FileInputStream x;
            String event = null;
            String fileName = namesArr[j] + ".txt";
            try {
                x = openFileInput(fileName);
                byte[] input = new byte[x.available()];
                while (x.read(input) != -1) {}
                event = new String(input, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(event!=null)
                eventList.add(new VolunteerEvent(event));
        }

        VolunteerEvent[] eventsArr = new VolunteerEvent[namesList.size()];
        eventList.toArray(eventsArr);

        ListAdapter customAdapter = new CustomAdapter(this, eventsArr);
        ListView customListView = (ListView) findViewById(R.id.customListView);
        customListView.setAdapter(customAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void arrCreator(String str)
    {
        if(str.indexOf(';')==-1)
            return;
        else if(str.indexOf(';')==(str.length()-1)){
            namesList.add(str.substring(0, (str.indexOf(';'))));
            return;
        }
        else
            namesList.add(str.substring(0, (str.indexOf(';'))));
        arrCreator(str.substring((str.indexOf(';'))+1));
        return;
    }
}
