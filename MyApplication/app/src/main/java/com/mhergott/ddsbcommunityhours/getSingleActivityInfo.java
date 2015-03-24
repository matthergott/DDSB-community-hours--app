package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.FileOutputStream;


public class getSingleActivityInfo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_get_single_activity_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Single Activity");

        RelativeLayout singleActivityLayout = new RelativeLayout(this);

        RelativeLayout.LayoutParams nameTextDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout.LayoutParams descriptionTextDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout.LayoutParams organisationTextDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout.LayoutParams hoursTextDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float pxHeight = outMetrics.heightPixels / density;
        double fourthOfHeight = (float) (pxHeight/5.0);

        final EditText nameTxt = new EditText(this);
        nameTxt.setId(1);
        final EditText description = new EditText(this);
        description.setId(2);
        final EditText organisation = new EditText(this);
        organisation.setId(3);
        final EditText hours = new EditText(this);
        hours.setId(4);

        nameTextDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        nameTextDetails.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        descriptionTextDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        descriptionTextDetails.addRule(RelativeLayout.BELOW, nameTxt.getId());
        descriptionTextDetails.setMargins(0, (int) fourthOfHeight, 0, 0);
        organisationTextDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        organisationTextDetails.addRule(RelativeLayout.BELOW, description.getId());
        organisationTextDetails.setMargins(0, (int) fourthOfHeight, 0, 0);
        hoursTextDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        hoursTextDetails.addRule(RelativeLayout.BELOW, organisation.getId());
        hoursTextDetails.setMargins(0, (int) fourthOfHeight, 0, 0);

        RelativeLayout.LayoutParams submitButtonDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        Button submit = new Button(this);
        submitButtonDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        submitButtonDetails.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        nameTxt.setText("Name of Event");
        description.setText("Brief Description");
        organisation.setText("Organisation");
        hours.setText("Hours Completed");
        submit.setText("Done");

        singleActivityLayout.addView(nameTxt, nameTextDetails);
        singleActivityLayout.addView(description, descriptionTextDetails);
        singleActivityLayout.addView(organisation, organisationTextDetails);
        singleActivityLayout.addView(hours, hoursTextDetails);
        singleActivityLayout.addView(submit, submitButtonDetails);
        setContentView(singleActivityLayout);

        submit.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){

                        String toFile =
                                nameTxt.getText().toString() + ";" +
                                description.getText().toString() + ";" +
                                organisation.getText().toString() + ";" +
                                hours.getText().toString() + ";";
                        String nameStr = (nameTxt.getText()).toString();

                        try {
                            FileOutputStream fos = openFileOutput("CompletedNames.txt", Context.MODE_APPEND);
                            fos.write((nameStr + ";").getBytes());
                            fos.close();
                        } catch (Exception e) {}
                        try {
                            FileOutputStream fos = openFileOutput(nameStr + ".txt", Context.MODE_APPEND);
                            fos.write((toFile).getBytes());
                            fos.close();
                        } catch (Exception e) {}

                        submitEvent(v);
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_single_activity_info, menu);
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

    public void submitEvent(View view){



        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
