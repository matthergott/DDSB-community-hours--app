package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class EventDetails extends ActionBarActivity {

    String nameTxt;
    String event;
    int position;
    ArrayList<String> hoursList;
    boolean recurring = false;
    LinearLayout linearListView;
    VolunteerEvent v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        linearListView = (LinearLayout) findViewById(R.id.linear_listview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Event Details");

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = savedInstanceState;
        }
        else{
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

        //Log.i("MyActivity", nameTxt);
        Log.i("MyActivity", event);
        //Log.i("MyActivity", v.getName());

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
            eventDatesAndHoursText.setVisibility(View.VISIBLE);//

            for (int i = 0; i < hoursList.size(); i++) {
                /**
                 * inflate items/ add items in linear layout instead of listview
                 */
                LayoutInflater inflater = null;
                inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView = inflater.inflate(R.layout.recurring_hours_row, null);
                TextView dateView = (TextView) mLinearView.findViewById(R.id.recurringDate);
                TextView hoursView = (TextView) mLinearView.findViewById(R.id.recurringHours);

                mLinearView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                ((LinearLayout) linearListView).addView((RelativeLayout) mLinearView);

                /**
                 * set item into row
                 */

                Log.i("MyActivity", hoursList.get(i));
                String str = hoursList.get(i);
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
                        // TODO Auto-generated method stub
                        Toast.makeText(EventDetails.this, "Clicked item;" + dateText,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu only if it is recurring
        if(recurring)
            getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: //this case was present when the activity was created
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_event_details_add_hours:
                Intent intent = new Intent(this,AddHoursRecurringEvent.class);
                intent.putExtra("event_details",event);
                startActivity(intent);
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
}
