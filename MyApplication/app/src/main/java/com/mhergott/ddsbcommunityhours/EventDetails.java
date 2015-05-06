package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EventDetails extends ActionBarActivity implements RecurringEventInstanceDialog.NoticeDialogListener, ConfirmDeleteDialog.NoticeDialogListener {

    String nameTxt;
    String event;
    int position;
    List<String> namesList = new ArrayList<String>();
    ArrayList<String> hoursList;
    boolean recurring = false;
    LinearLayout linearListView;
    VolunteerEvent v;
    static final String STATE_EVENT = "passedEvent";
    static final String STATE_POSITION = "passedPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        linearListView = (LinearLayout) findViewById(R.id.linear_listview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Event Details");

        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null) { // if there was no bundle passed to the activity, use the saved state
            // Restore value of members from saved state
            nameTxt = savedInstanceState.getString(STATE_EVENT);
            position = savedInstanceState.getInt(STATE_POSITION);
        }
        if(bundle!=null){
            // Use values passed to activity in a bundle
            nameTxt = bundle.getString("event_name");
            position = bundle.getInt("tab");
        }

        FileInputStream fis;
        try {
            fis = openFileInput(nameTxt + ".txt");
            byte[] input = new byte[fis.available()];
            while (fis.read(input) != -1) {}
            event = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        v = new VolunteerEvent(event,position);

        TextView name = (TextView) findViewById(R.id.nameTextEventDetails);
        name.setText(v.getName().toString());
        TextView description = (TextView) findViewById(R.id.descriptionTextEventDetails);
        description.setText(v.getDescription().toString());
        TextView organisation = (TextView) findViewById(R.id.organisationTextEventDetails);
        organisation.setText(v.getOrganisation().toString());
        TextView hours = (TextView) findViewById(R.id.hoursTextEventDetails);
        hours.setText(v.getHours().toString());
        //check if there are any recurring events listed in the volunteer event passed in
        if(v.getHoursList()!=null) {
            hoursList = v.getHoursList();
            recurring = true;
            TextView eventDatesAndHoursText = (TextView) findViewById(R.id.eventDatesAndHoursText);
            if(v.getHoursList().size()>0)
                eventDatesAndHoursText.setVisibility(View.VISIBLE);

            for (int i = 0; i < hoursList.size(); i++) {
                //inflate items/ add items in linear layout instead of listview
                LayoutInflater inflater = null;
                inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView = inflater.inflate(R.layout.recurring_hours_row, null);
                TextView dateView = (TextView) mLinearView.findViewById(R.id.recurringDate);
                TextView hoursView = (TextView) mLinearView.findViewById(R.id.recurringHours);

                mLinearView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                ((LinearLayout) linearListView).addView((RelativeLayout) mLinearView);

                //set item into row
                Log.i("MyActivity", hoursList.get(i));
                String str = hoursList.get(i);
                final String toBeDeleted = str;
                final String dateText = str.substring(0, str.indexOf(';'));
                str = str.substring(str.indexOf(';') + 1);
                final String hoursText = str.substring(0, str.indexOf(';'));

                dateView.setText(dateText);
                dateView.setTextColor(Color.BLACK);
                hoursView.setText(hoursText);
                hoursView.setTextColor(Color.BLACK);

                //get item row on click
                mLinearView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewRecurringEventInstance(dateText, hoursText, toBeDeleted);
                    }
                });
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the event and position passed into the activity
        savedInstanceState.putString(STATE_EVENT, nameTxt);
        savedInstanceState.putInt(STATE_POSITION, position);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu only if it is recurring
        if(recurring)
            getMenuInflater().inflate(R.menu.menu_event_details_recurring, menu);
        else
            getMenuInflater().inflate(R.menu.menu_event_details_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: //for up navigation
                Intent i = new Intent(this,MainActivity.class);
                i.putExtra("position_value",position);
                startActivity(i);
                return true;
            case R.id.menu_event_details_add_hours:
                Intent j = new Intent(this,AddHoursRecurringEvent.class);
                j.putExtra("event_details",event);
                startActivity(j);
                return true;
            case R.id.menu_event_details_delete:
                confirmDelete();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void ViewRecurringEventInstance(String date, String hours, String toDelete){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("recurring_event_instance_date", date);
        editor.putString("recurring_event_instance_hours", hours);
        editor.putString("recurring_event_instance_toDelete", toDelete);
        editor.commit();

        DialogFragment f = new RecurringEventInstanceDialog();
        f.show(getSupportFragmentManager(), "recurringEventInstance");
    }
    @Override
    public void onDialogDelete(DialogFragment dialog) {
        confirmDelete();
        return;
    }
    @Override
    public void onDialogCancel(DialogFragment dialog) {
        return;
    }

    public void confirmDelete(){
        DialogFragment g = new ConfirmDeleteDialog();
        g.show(getSupportFragmentManager(), "confirmDelete");
    }
    @Override
    public void onDialogDeleteConfirm(DialogFragment dialog) {
        if(recurring) {
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            String toDelete = sharedPref.getString("recurring_event_instance_toDelete", "");

            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_PRIVATE);
                fos.write(v.removeHours(toDelete).getBytes());
                fos.close();
            } catch (Exception e) {
            }

            finish();
            Intent intent = new Intent(this, EventDetails.class);
            intent.putExtra("event_name", v.getName());
            intent.putExtra("tab", 0);
            startActivity(intent);
        }
        else {
            FileInputStream fis;
            String currentNames = "";
            try {
                fis = openFileInput("CompletedNames.txt");
                byte[] input = new byte[fis.available()];
                while (fis.read(input) != -1) {
                }
                currentNames = new String(input, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            listCreator(currentNames);

            //remove the event from the list, make the list a string again, then resave the list string
            namesList.remove(v.getName());
            String toFile = "";
            for (int a = 0; a < namesList.size(); a++) {
                toFile = toFile + namesList.get(a) + ";";
            }

            if (toFile.equals("")) {
                deleteFile("CompletedNames.txt");
            }
            else {
                try {
                    FileOutputStream fos = openFileOutput("CompletedNames.txt", Context.MODE_PRIVATE);
                    fos.write(toFile.getBytes());
                    fos.close();
                } catch (Exception e) {
                }
            }

            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("position_value",1);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onDialogCancelConfirm(DialogFragment dialog) {
        return;
    }

    private void listCreator(String str){
        while(str.indexOf(';')!=-1){
            namesList.add(str.substring(0, str.indexOf(';')));
            str = str.substring(str.indexOf(';') + 1);
        }
    }
}
