package com.mhergott.ddsbcommunityhours;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener {

    private static final String TAG = "mattsMessage";
    List currentNamesList = new ArrayList();
    String currentNames;
    VolunteerEvent[] currentEventsArr;
    List completedNamesList = new ArrayList();
    String completedNames;
    VolunteerEvent[] completedEventsArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        try {
            FileOutputStream fos = openFileOutput("CurrentNames.txt", Context.MODE_PRIVATE);
            fos.write("Swimming;".getBytes());
            fos.close();
        } catch (Exception e) {}
        try {
            FileOutputStream fos = openFileOutput("Swimming.txt", Context.MODE_PRIVATE);
            fos.write("Swimming;frolicking in the water;City of Pickering;17;".getBytes());
            fos.close();
        } catch (Exception e) {}
        try {
            FileOutputStream fos = openFileOutput("CompletedNames.txt", Context.MODE_PRIVATE);
            fos.write("Landscaping;".getBytes());
            fos.close();
        } catch (Exception e) {}
        try {
            FileOutputStream fos = openFileOutput("Landscaping.txt", Context.MODE_PRIVATE);
            fos.write("Landscaping;Lifting rocks on the regs;All Pro;500;".getBytes());
            fos.close();
        } catch (Exception e) {}
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volunteer Hours");
        actionBar.addTab(actionBar.newTab().setText("In Progress").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Submitted").setTabListener(this));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //access completed and current names files
        FileInputStream fis;
        try {
            fis = openFileInput("CurrentNames.txt");
            byte[] input = new byte[fis.available()];
            while (fis.read(input) != -1) {}
            currentNames = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(currentNames!=null)
            arrCreator(currentNames, true);
        String[] currentNamesArr = new String[currentNamesList.size()];
        currentNamesList.toArray(currentNamesArr);

        FileInputStream fis2;
        try {
            fis2 = openFileInput("CompletedNames.txt");
            byte[] input = new byte[fis2.available()];
            while (fis2.read(input) != -1) {}
            completedNames = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(completedNames!=null)
            arrCreator(completedNames, false);
        String[] completedNamesArr = new String[completedNamesList.size()];
        completedNamesList.toArray(completedNamesArr);
        //////////////////////////////////////////////////////////////////////////////////

        //access individual event files and save into an object array
        ArrayList<VolunteerEvent> currentEventList = new ArrayList<VolunteerEvent>();
        for(int j = 0; j < currentNamesArr.length; j++){
            //Read file in Internal Storage
            FileInputStream x;
            String event = null;
            String fileName = currentNamesArr[j] + ".txt";
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
                currentEventList.add(new VolunteerEvent(event));
        }
        currentEventsArr = new VolunteerEvent[currentNamesList.size()];
        currentEventList.toArray(currentEventsArr);

        ArrayList<VolunteerEvent> completedEventList = new ArrayList<VolunteerEvent>();
        for(int j = 0; j < completedNamesArr.length; j++){
            //Read file in Internal Storage
            FileInputStream x;
            String event = null;
            String fileName = completedNamesArr[j] + ".txt";
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
                completedEventList.add(new VolunteerEvent(event));
        }
        completedEventsArr = new VolunteerEvent[completedNamesList.size()];
        completedEventList.toArray(completedEventsArr);
        //////////////////////////////////////////////////////////////////////////

        //add current events array as default to the list view
        ListAdapter customAdapter = new CustomAdapter(this, currentEventsArr);
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

    public void arrCreator(String str, boolean x)
    {
        if(x) {
            if (str.indexOf(';') == -1)
                return;
            else if (str.indexOf(';') == (str.length() - 1)) {
                currentNamesList.add(str.substring(0, (str.indexOf(';'))));
                return;
            } else
                currentNamesList.add(str.substring(0, (str.indexOf(';'))));
            arrCreator(str.substring((str.indexOf(';')) + 1), x);
            return;
        }
        else{
            if (str.indexOf(';') == -1)
                return;
            else if (str.indexOf(';') == (str.length() - 1)) {
                completedNamesList.add(str.substring(0, (str.indexOf(';'))));
                return;
            } else
                completedNamesList.add(str.substring(0, (str.indexOf(';'))));
            arrCreator(str.substring((str.indexOf(';')) + 1), x);
            return;
        }
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if(tab.getPosition()==0){
            setContentView(R.layout.activity_main);
            if(currentEventsArr!=null) {
                ListAdapter customAdapter = new CustomAdapter(this, currentEventsArr);
                ListView customListView = (ListView) findViewById(R.id.customListView);
                customListView.setAdapter(customAdapter);
            }
        }
        else{
            setContentView(R.layout.activity_main);
            if(completedEventsArr!=null) {
                ListAdapter customAdapter = new CustomAdapter(this, completedEventsArr);
                ListView customListView = (ListView) findViewById(R.id.customListView);
                customListView.setAdapter(customAdapter);
            }
        }
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
