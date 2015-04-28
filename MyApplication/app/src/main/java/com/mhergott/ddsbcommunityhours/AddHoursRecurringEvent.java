package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

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
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = savedInstanceState;
        }
        else{
            event = bundle.getString("event_details");
        }

        v = new VolunteerEvent(event,0);

        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR); // get the current year
        int currentMonth = cal.get(Calendar.MONTH) + 1; // month is 0 based so 1 must be added
        int currentDay = cal.get(Calendar.DAY_OF_MONTH); // current day in the month

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

        //create array for year using the current year
        String[] yearArray = new String[6];
        yearArray[0] = "[year]";
        for(int a = 1, yearCopy = currentYear; a<6; a++, yearCopy--)
            yearArray[a] = String.valueOf(yearCopy);

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
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearArray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapter3);
        year.setOnItemSelectedListener(this);

        day.setSelection(getIndex(day, String.valueOf(currentDay))); //set spinners to current date
        month.setSelection(getIndex(month, monthConverter(currentMonth)));
        //month.setSelection(getIndex(month, "April"));
        year.setSelection(getIndex(year, String.valueOf(currentYear)));

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
        add.setOnLongClickListener(new View.OnLongClickListener() { //needs work!
            @Override
            public boolean onLongClick(View v) {
                counter++;
                display.setText("" + counter);
                return false;
            }
        });
        sub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                counter--;
                if(counter < 0){
                    counter++;
                    return;
                }
                display.setText("" + counter);
            }
        });
    }

    private String monthConverter(int month) {
        String str = "";

        switch(month){
            case 1:
                str = "January";
                break;
            case 2:
                str = "February";
                break;
            case 3:
                str = "March";
                break;
            case 4:
                str = "April";
                break;
            case 5:
                str = "May";
                break;
            case 6:
                str = "June";
                break;
            case 7:
                str = "July";
                break;
            case 8:
                str = "August";
                break;
            case 9:
                str = "September";
                break;
            case 10:
                str = "October";
                break;
            case 11:
                str = "November";
                break;
            case 12:
                str = "December";
                break;
            default:
                str = "[month]";
                break;
        }

        return str;
    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    public void submitAddHoursRecurring(View view) {
        daySelected = day.getSelectedItem().toString();
        monthSelected = month.getSelectedItem().toString();
        yearSelected = year.getSelectedItem().toString();

        boolean isError = false;
        if (counter<=0) {
            isError = true;
            display.setBackgroundColor(Color.RED);
        }
        if (daySelected == null || daySelected.equals("[day]")) {
            isError = true;
            day.setBackgroundColor(Color.RED);
        }
        if (monthSelected == null || monthSelected.equals("[month]")) {
            isError = true;
            month.setBackgroundColor(Color.RED);
        }
        if (yearSelected == null || yearSelected.equals("[year]")) {
            isError = true;
            year.setBackgroundColor(Color.RED);
        }

        if (!isError) {
            String toFile = daySelected + " " + monthSelected + " " + yearSelected + ";" + counter + ";";
            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_PRIVATE);
                fos.write(v.addHours(toFile).getBytes());
                fos.close();
            } catch (Exception e) {
            }
            Intent intent = new Intent(this, EventDetails.class);
            intent.putExtra("event_name", v.getName());
            intent.putExtra("tab", 0);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(AddHoursRecurringEvent.this, "Please update all red fields",
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_hours_recurring_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // for up navigation
            Intent intent = new Intent(this,EventDetails.class);
            intent.putExtra("event_name",v.getName());
            intent.putExtra("tab",0);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //required empty method
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //required empty method
    }
}
