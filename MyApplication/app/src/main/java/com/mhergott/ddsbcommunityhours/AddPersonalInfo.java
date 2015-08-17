package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
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
import java.util.Calendar;


public class AddPersonalInfo extends ActionBarActivity implements AdapterView.OnItemSelectedListener,
        TutorialDialog1.NoticeDialogListener,TutorialDialog3.NoticeDialogListener, TutorialDialog4.NoticeDialogListener,
        TutorialDialog5.NoticeDialogListener, TutorialDialog6.NoticeDialogListener {

    String schoolSelected;
    String daySelected;
    String monthSelected;
    String yearSelected;
    Spinner schools;
    Spinner day;
    Spinner month;
    Spinner year;
    private String personalInformation;
    private String personalInformationCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_info);

        //create and rename the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Personal Info");
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        //code for creating the spinners
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
        //get the current year, then subract 13 to all kids' DOBs that could be in high school
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR); // get the current year

        //create array for year using the current year value
        String[] yearArray = new String[8];
        yearArray[0] = "[year]";
        for(int a = 1, yearCopy = (currentYear-13); a<8; a++, yearCopy--)
            yearArray[a] = String.valueOf(yearCopy);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearArray);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapter3);
        year.setOnItemSelectedListener(this);

        //////////////////////////////////////if entering page from "view info" dialog the following will execute
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
        personalInformationCopy = personalInformation;
        if(personalInformation!=null){ //check if there is information already saved and treat the text file
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
            schools.setSelection(getIndex(schools, schoolTxt)); //set spinners to information that is already saved
            day.setSelection(getIndex(day, dayTxt));
            month.setSelection(getIndex(month, monthTxt));
            year.setSelection(getIndex(year, yearTxt));
        }
    }

    public void personalInfoButtonClick(View view){

        //get the info from the fields
        EditText namePersonal = (EditText) findViewById(R.id.name_personal);
        String name = namePersonal.getText().toString();
        schoolSelected = schools.getSelectedItem().toString();
        daySelected = day.getSelectedItem().toString();
        monthSelected = month.getSelectedItem().toString();
        yearSelected = year.getSelectedItem().toString();

        //if the inputted info is invalid, print an error
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

            if(personalInformationCopy == null){
                launchTutorial();
            }
            else {
                returnToMainActivity();
            }
        }
        else{
            Toast.makeText(AddPersonalInfo.this, "Please update all red fields",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void returnToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
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

    //Launch the series of tutorial dialogs if there was no information previously entered in system.
    private void launchTutorial() {
        DialogFragment frag = new TutorialDialog1();
        frag.show(getSupportFragmentManager(), "confirmDelete");
    }
    @Override
    public void onDialogNext(DialogFragment dialog) {
        DialogFragment frag = new TutorialDialog3();
        frag.show(getSupportFragmentManager(), "confirmDelete");
    }
//    @Override
//    public void onDialogNext2(DialogFragment dialog) {
////        DialogFragment frag = new TutorialDialog4();
////        frag.show(getSupportFragmentManager(), "confirmDelete");
//    }
    @Override
    public void onDialogNext3(DialogFragment dialog) {
        DialogFragment frag = new TutorialDialog4();
        frag.show(getSupportFragmentManager(), "confirmDelete");
    }
    @Override
    public void onDialogNext4(DialogFragment dialog) {
        DialogFragment frag = new TutorialDialog5(); //keep designing the fifth one!!!!!
        frag.show(getSupportFragmentManager(), "confirmDelete");
    }

    @Override
    public void onDialogNext5(DialogFragment dialog) {
        DialogFragment frag = new TutorialDialog6(); //keep designing the fifth one!!!!!
        frag.show(getSupportFragmentManager(), "confirmDelete");

    }

    @Override
    public void onDialogNext6(DialogFragment dialog) {
		returnToMainActivity();
		Toast.makeText(getApplicationContext(), "Click the three dots up top to get started",
							  Toast.LENGTH_LONG).show();
		finish();
    }
}
