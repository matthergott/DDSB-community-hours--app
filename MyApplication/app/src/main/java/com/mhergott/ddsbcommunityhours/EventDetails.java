package com.mhergott.ddsbcommunityhours;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class EventDetails extends ActionBarActivity {

    String nameTxt;
    String event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Event Details");

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = savedInstanceState;
        }
        else{
            nameTxt = bundle.getString("event_name");
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

        VolunteerEvent v = new VolunteerEvent(event);

        TextView name = (TextView) findViewById(R.id.nameText);
        name.setText(v.getName());
        TextView description = (TextView) findViewById(R.id.descriptionText);
        description.setText(v.getDescription());
        TextView organisation = (TextView) findViewById(R.id.organisationText);
        organisation.setText(v.getOrganisation());
        TextView hours = (TextView) findViewById(R.id.hoursText);
        hours.setText(v.getHours());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
