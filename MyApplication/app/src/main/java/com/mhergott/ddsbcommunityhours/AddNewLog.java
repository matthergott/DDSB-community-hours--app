package com.mhergott.ddsbcommunityhours;

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
import android.widget.RelativeLayout;


public class AddNewLog extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_new_log);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add a New Log");

        /////////////////////////////////////////////////////////////////
        RelativeLayout addLogLayout = new RelativeLayout(this);

        RelativeLayout.LayoutParams singleButtonDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        RelativeLayout.LayoutParams recurringButtonDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        double thirdOfHeight = (float) (dpHeight/3.0);

        Button singleButton = new Button(this);
        Button recurringButton = new Button(this);
        singleButton.setId(1);
        recurringButton.setId(2);

        singleButtonDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        singleButtonDetails.setMargins(0, (int) thirdOfHeight, 0, 0);
        recurringButtonDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        recurringButtonDetails.addRule(RelativeLayout.BELOW, singleButton.getId());
        recurringButtonDetails.setMargins(0, (int) thirdOfHeight, 0, 0);

        singleButton.setText("Single-Time Activity");
        recurringButton.setText("Recurring Activity");

        addLogLayout.addView(singleButton, singleButtonDetails);
        addLogLayout.addView(recurringButton, recurringButtonDetails);
        setContentView(addLogLayout);

        singleButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        goToSingleActivity(v);
                    }
                }
        );
        /////////////////////////////////////////////////////////////////
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_log, menu);
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
    public void goToSingleActivity(View view){
        Intent intent = new Intent(this,GetSingleActivityInfo.class);
        startActivity(intent);
    }
    public void goToRecurringActivity(View view){
       // Intent intent = new Intent(this,DisplayMessageActivity.class);
    }
}
