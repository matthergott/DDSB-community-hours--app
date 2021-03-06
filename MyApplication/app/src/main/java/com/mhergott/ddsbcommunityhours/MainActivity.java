package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener, SelectLogTypeDialog.NoticeDialogListener, ViewPersonalInfoDialog.NoticeDialogListener {

    private static final String TAG = "mattsMessage";
    List currentNamesList = new ArrayList();
    String currentNames;
    VolunteerEvent[] currentEventsArr;
    List completedNamesList = new ArrayList();
    String completedNames;
    VolunteerEvent[] completedEventsArr;
    private Context context = null;
    String personalInformation;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Determine which bundle to use; either the saved instance or a bundle
        // that has been passed in through an intent.
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = savedInstanceState;
        }
        // Initialize members with bundle or default values.
        if (bundle != null) {
            position = bundle.getInt("position_value");
        } else {
            position = 0;
        }

        FileInputStream fis2;
        try {
            fis2 = openFileInput("personalInfo.txt");
            byte[] input = new byte[fis2.available()];
            while (fis2.read(input) != -1) {}
            personalInformation = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(personalInformation==null){
            Intent j = new Intent(this, AddPersonalInfo.class);
            startActivity(j);
            finish();
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Volunteer Hours");
        actionBar.addTab(actionBar.newTab().setText("Recurring").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Single Time").setTabListener(this));
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
        final String[] currentNamesArr = new String[currentNamesList.size()];
        currentNamesList.toArray(currentNamesArr);

        FileInputStream fis3;
        try {
            fis3 = openFileInput("CompletedNames.txt");
            byte[] input = new byte[fis3.available()];
            while (fis3.read(input) != -1) {}
            completedNames = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(completedNames!=null)
            arrCreator(completedNames, false);
        final String[] completedNamesArr = new String[completedNamesList.size()];
        completedNamesList.toArray(completedNamesArr);

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

        //add current events array as default to the list view
        ListAdapter customAdapter = new MainCustomAdapter(this, currentEventsArr);
        ListView customListView = (ListView) findViewById(R.id.customListView);
        customListView.setAdapter(customAdapter);

        customListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        VolunteerEvent v = (VolunteerEvent) parent.getItemAtPosition(position);
                        String str = v.getTimeStamp();
                        displayEvent(view, str, actionBar.getSelectedTab());
                    }
                }
        );

        int totalHours = 0;
        if(currentEventsArr!=null) {
            for (int a = 0; a < currentEventsArr.length; a++) {
                totalHours += Integer.valueOf((currentEventsArr[a]).getHours());
            }
        }
        if(completedEventsArr!=null) {
            for (int a = 0; a < completedEventsArr.length; a++) {
                totalHours += Integer.valueOf((completedEventsArr[a]).getHours());
            }
        }
        setHours(String.valueOf(totalHours));

        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onBackPressed() {
        //so that the back button on the actual device does not work when the main activity is up
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.main_add_new_log:
                selectEventType();
                return true;
            case R.id.main_view_personal_info:
                viewPersonalInfo();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void arrCreator(String str, boolean x)
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
            if(currentEventsArr!=null) {
                ListAdapter customAdapter = new MainCustomAdapter(this, currentEventsArr);
                ListView customListView = (ListView) findViewById(R.id.customListView);
                customListView.setAdapter(customAdapter);
            }
        }
        else{
            if(completedEventsArr!=null) {
                ListAdapter customAdapter = new MainCustomAdapter(this, completedEventsArr);
                ListView customListView = (ListView) findViewById(R.id.customListView);
                customListView.setAdapter(customAdapter);
            }
        }
    }
    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}


    public void setHours(String str){
        MainActivityFooter foot = (MainActivityFooter) getSupportFragmentManager().findFragmentById(R.id.footer);
        foot.setHours(str);
    }


    public void selectEventType(){
        DialogFragment newFragment = new SelectLogTypeDialog();
        newFragment.show(getSupportFragmentManager(), "newLog");
    }
    @Override
    public void onDialogSingleClick(DialogFragment dialog) {
        Intent intent = new Intent(this,GetSingleActivityInfo.class);
        startActivity(intent);
    }
    @Override
    public void onDialogRecurringClick(DialogFragment dialog) {
        Intent intent = new Intent(this,GetRecurringActivityInfo.class);
        startActivity(intent);
    }

    public void displayEvent(View view, String str, android.support.v7.app.ActionBar.Tab tab){
        int tabSelected = tab.getPosition();
        Intent intent = new Intent(this,EventDetails.class);
        intent.putExtra("event_time_stamp",str);
        intent.putExtra("tab",tabSelected);
        startActivity(intent);
    }

    public void viewPersonalInfo(){
        DialogFragment f = new ViewPersonalInfoDialog();
        f.show(getSupportFragmentManager(), "personalInfo");
    }
    @Override
    public void onDialogEditInfo(DialogFragment dialog) {
        Intent intent = new Intent(this,AddPersonalInfo.class);
        startActivity(intent);
    }
    @Override
    public void onDialogCancel(DialogFragment dialog) {
        return;
    }
}
