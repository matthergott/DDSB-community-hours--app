package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddHoursRecurringEvent extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private Spinner day;
    private Spinner month;
    private Spinner year;
    String daySelected = "";
    String monthSelected = "";
    String yearSelected = "";
    private int counter = 0;
    private Button add;
    private Button sub;
    private TextView display;
    private Button submit;
    String event;
    VolunteerEvent v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hours_recurring_event);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Hours");

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = savedInstanceState;
        }
        else{
            event = bundle.getString("event_details");
        }

        v = new VolunteerEvent(event,0);

        //code for date picking spinners
        day = (Spinner) findViewById(R.id.daySpinnerAddHours);
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                daySelected = arg0.getItemAtPosition(position).toString();//saving the value selected
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(adapter1);
        day.setOnItemSelectedListener(this);

        month = (Spinner) findViewById(R.id.monthSpinnerAddHours);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                monthSelected = arg0.getItemAtPosition(position).toString();//saving the value selected
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(adapter2);
        month.setOnItemSelectedListener(this);

        year = (Spinner) findViewById(R.id.yearSpinnerAddHours);
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                yearSelected = arg0.getItemAtPosition(position).toString();//saving the value selected
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapter3);
        year.setOnItemSelectedListener(this);

        //code for number picker
        counter = 0;
        add = (Button) findViewById(R.id.bAdd);
        sub = (Button) findViewById(R.id.bSub);
        display = (TextView) findViewById(R.id.numPickerDisplay);
        display.setText("" + counter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                display.setText("" + counter);
            }
        });
        sub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                counter--;
                display.setText("" + counter);
            }
        });
    }

    public void submitAddHoursRecurring(View view){
        daySelected = day.getSelectedItem().toString();
        monthSelected = month.getSelectedItem().toString();
        yearSelected = year.getSelectedItem().toString();

        String toFile = daySelected + " " + monthSelected + " " + yearSelected + ";" + counter + ";";

        Log.i("MyActivity", v.getName());
        Log.i("MyActivity", toFile);

        try {
            FileOutputStream fos = openFileOutput(v.getName()+".txt", Context.MODE_PRIVATE);
            fos.write(v.addHours(toFile).getBytes());
            fos.close();
        } catch (Exception e) {}

        Intent intent = new Intent(this,EventDetails.class);
        intent.putExtra("event_name",v.getName());
        intent.putExtra("tab",0);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_hours_recurring_event, menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
