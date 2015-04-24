package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class AddPersonalInfo extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    String schoolSelected;
    String daySelected;
    String monthSelected;
    String yearSelected;
    Spinner schools;
    Spinner day;
    Spinner month;
    Spinner year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Personal Info");
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        schools = (Spinner) findViewById(R.id.schoolsSpinner);
        schools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                schoolSelected = arg0.getItemAtPosition(position).toString();//saving the value selected
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.schools, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schools.setAdapter(adapter);
        schools.setOnItemSelectedListener(this);

        day = (Spinner) findViewById(R.id.daySpinner);
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

        month = (Spinner) findViewById(R.id.monthSpinner);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                monthSelected = arg0.getItemAtPosition(position).toString();;//saving the value selected
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(adapter2);
        month.setOnItemSelectedListener(this);

        year = (Spinner) findViewById(R.id.yearSpinner);
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

        //////////////////////////////////////if entering page from "view info" dialog the following will execute
        String personalInformation = null;
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
        if(personalInformation!=null){
            String nameTxt = personalInformation.substring(0, personalInformation.indexOf(";"));
            personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
            String schoolTxt = personalInformation.substring(0, personalInformation.indexOf(";"));
            personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
            String dayTxt = personalInformation.substring(0, personalInformation.indexOf(";"));
            personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
            String monthTxt = personalInformation.substring(0, personalInformation.indexOf(";"));
            personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
            String yearTxt = personalInformation.substring(0, personalInformation.indexOf(";"));

            EditText namePersonal = (EditText) findViewById(R.id.name_personal);
            namePersonal.setText(nameTxt);
            schools.setSelection(getIndex(schools, schoolTxt));
            day.setSelection(getIndex(day, dayTxt));
            month.setSelection(getIndex(month, monthTxt));
            year.setSelection(getIndex(year, yearTxt));
        }
    }

    public void personalButtonClick(View view){

        EditText namePersonal = (EditText) findViewById(R.id.name_personal);
        String name = namePersonal.getText().toString();
        schoolSelected = schools.getSelectedItem().toString();
        daySelected = day.getSelectedItem().toString();
        monthSelected = month.getSelectedItem().toString();
        yearSelected = year.getSelectedItem().toString();

        boolean isError = false;
        if(name.equals("")){
            isError = true;
            namePersonal.setBackgroundColor(Color.RED);
            namePersonal.setHint("Please enter name of event");
        }
        if(schoolSelected==null||schoolSelected.equals("[Choose a School]")){
            isError = true;
            schools.setBackgroundColor(Color.RED);
        }
        if(daySelected==null||daySelected.equals("[day]")){
            isError = true;
            day.setBackgroundColor(Color.RED);
        }
        if(monthSelected==null||monthSelected.equals("[month]")){
            isError = true;
            month.setBackgroundColor(Color.RED);
        }
        if(yearSelected==null||yearSelected.equals("[year]")){
            isError = true;
            year.setBackgroundColor(Color.RED);
        }

        if(!isError){
            String toFile = name + ";" + schoolSelected + ";" + daySelected + ";" + monthSelected + ";" + yearSelected + ";";
            String FILENAME = "personalInfo.txt";
            try {
                FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write(toFile.getBytes());
                fos.close();
            } catch (Exception e) {}

            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            prefEditor.putString("com.mhergott.ddsbcommunityhours.name", name);
            prefEditor.putString("com.mhergott.ddsbcommunityhours.school", schoolSelected);
            prefEditor.putString("com.mhergott.ddsbcommunityhours.day", daySelected);
            prefEditor.putString("com.mhergott.ddsbcommunityhours.month", monthSelected);
            prefEditor.putString("com.mhergott.ddsbcommunityhours.year", yearSelected);
            prefEditor.commit();

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else{
            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_personal_info, menu);
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
}
