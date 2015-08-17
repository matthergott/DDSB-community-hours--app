package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;


public class GetRecurringActivityInfo extends ActionBarActivity {

    private String ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recurring_activity_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Recurring Activity");

        final EditText nameTxt = (EditText) findViewById(R.id.name_recur);
        final EditText description = (EditText) findViewById(R.id.description_recur);
        final EditText organisation = (EditText) findViewById(R.id.organisation_recur);
        final EditText supervisorName = (EditText) findViewById(R.id.supervisor_name_recur);
        final EditText telephoneNumber = (EditText) findViewById(R.id.telephone_recur);
        Button submit = (Button) findViewById(R.id.submit_recur);

        submit.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        boolean isError = false;

                        if (nameTxt.getText().toString().equals("")) {
                            isError = true;
                            nameTxt.setBackgroundColor(Color.RED);
                            nameTxt.setHint("Please enter name of event");
                        } else {
                            nameTxt.setBackgroundColor(Color.TRANSPARENT);
                        }
                        if (description.getText().toString().equals("")) {
                            isError = true;
                            description.setBackgroundColor(Color.RED);
                            description.setHint("Please enter description of event");
                        } else {
                            description.setBackgroundColor(Color.TRANSPARENT);
                        }
                        if (organisation.getText().toString().equals("")) {
                            isError = true;
                            organisation.setBackgroundColor(Color.RED);
                            organisation.setHint("Please enter organisation");
                        } else {
                            organisation.setBackgroundColor(Color.TRANSPARENT);
                        }
                        if(supervisorName.getText().toString().equals("")) {
                            isError = true;
                            supervisorName.setBackgroundColor(Color.RED);
                            supervisorName.setHint("Please enter supervisor's name");
                        }
                        else{
                            supervisorName.setBackgroundColor(Color.TRANSPARENT);
                        }
                        if(telephoneNumber.getText().toString().length()!=10) {
                            isError = true;
                            telephoneNumber.setBackgroundColor(Color.RED);
                            telephoneNumber.setHint("Please enter 10 digit phone number");
                        }
                        else{
                            telephoneNumber.setBackgroundColor(Color.TRANSPARENT);
                        }
                        if(!isError) {
                            Long tsLong = System.currentTimeMillis()/1000;
                            ts = tsLong.toString();
                            String toFile = ts + ";" +
                                    "No candid photo present;No signature photo present;Not submitted;" +
                                        nameTxt.getText().toString() + ";" +
                                        description.getText().toString() + ";" +
                                        organisation.getText().toString() + ";" +
                                        supervisorName.getText().toString() + ";" +
                                        telephoneNumber.getText().toString() + ";" +
                                        "0;";
                            String nameStr = (nameTxt.getText()).toString();

                            try {
                                FileOutputStream fos = openFileOutput("CurrentNames.txt", Context.MODE_APPEND);
                                fos.write((ts + ";").getBytes());
                                fos.close();
                            } catch (Exception e) {
                            }
                            try {
                                FileOutputStream fos = openFileOutput(ts + ".txt", Context.MODE_APPEND);
                                fos.write((toFile).getBytes());
                                fos.close();
                            } catch (Exception e) {
                            }

                            submitEvent(v);
                        }
                        else
                            return;
                    }
                }
            );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_recurring_activity_info, menu);
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
    private void submitEvent(View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("position_value",0);
        startActivity(intent);
        finish();
    }
}
