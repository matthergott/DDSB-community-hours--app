package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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


public class GetSingleActivityInfo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_single_activity_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Single Activity");


        final EditText nameTxt = (EditText) findViewById(R.id.name_single);
        final EditText description = (EditText) findViewById(R.id.description_single);
        final EditText organisation = (EditText) findViewById(R.id.organisation_single);
        final EditText hours = (EditText) findViewById(R.id.hours_single);
        Button submit = (Button) findViewById(R.id.submit_single);


        submit.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){

                        boolean isError = false;

                        if(nameTxt.getText().toString().equals("")) {
                            isError = true;
                            nameTxt.setBackgroundColor(Color.RED);
                            nameTxt.setHint("Please enter name of event");
                        }
                        else{
                            nameTxt.setBackgroundColor(Color.TRANSPARENT);
                        }
                        if(description.getText().toString().equals("")) {
                            isError = true;
                            description.setBackgroundColor(Color.RED);
                            description.setHint("Please enter description of event");
                        }
                        else{
                            description.setBackgroundColor(Color.TRANSPARENT);
                        }
                        if(organisation.getText().toString().equals("")) {
                            isError = true;
                            organisation.setBackgroundColor(Color.RED);
                            organisation.setHint("Please enter organisation");
                        }
                        else{
                            organisation.setBackgroundColor(Color.TRANSPARENT);
                        }
                        if(hours.getText().toString().equals("")) {
                            isError = true;
                            hours.setBackgroundColor(Color.RED);
                            hours.setHint("Please enter hours completed");
                        }
                        else{
                            hours.setBackgroundColor(Color.TRANSPARENT);
                        }

                        for (int a = 0; a < hours.getText().toString().length(); a++){
                            int x = (int) hours.getText().toString().charAt(a);
                            if(x<48 || x>57) {
                                isError = true;
                                hours.setBackgroundColor(Color.RED);
                                hours.setText("");
                                hours.setHint("Please enter hours completed");
                                a = hours.getText().toString().length();
                            }
                        }
                        if(Integer.valueOf(hours.getText().toString())<=0){
                            isError = true;
                            hours.setBackgroundColor(Color.RED);
                            hours.setText("");
                            hours.setHint("Please enter hours completed");
                        }

                        if(!isError) {
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
                            } catch (Exception e) {
                            }
                            try {
                                FileOutputStream fos = openFileOutput(nameStr + ".txt", Context.MODE_APPEND);
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
        intent.putExtra("position_value",1);
        startActivity(intent);
        finish();
    }
}
