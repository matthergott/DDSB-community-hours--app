package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
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

public class AddHoursRecurringEvent extends ActionBarActivity implements AdapterView.OnItemSelectedListener, ConfirmAddCandidShotDialog.NoticeDialogListener {

    private static final int CANDID_CAMERA_REQUEST = 22221;
    private static final int CANDID_RESULT_LOAD_IMAGE = 12222;
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
    private Bitmap candidPhoto;
    private String selectedCandidImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hours_recurring_event);

        //edit the action bar to change title and add 'back' button
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Hours");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //receive the information from the bundle
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
        year.setSelection(getIndex(year, String.valueOf(currentYear)));

        //code for number picker
        counter = 0;
        add = (Button) findViewById(R.id.bAdd);
        sub = (Button) findViewById(R.id.bSub);
        display = (TextView) findViewById(R.id.numPickerDisplay);
        display.setText("" + counter);

        //when plus button is clicked, add 1 to counter
        //when minus button is clicked, subtract one, make sure no negative hours
        add.setOnTouchListener(new NumberPickerListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                display.setText("" + counter);
            }
        }));
        sub.setOnTouchListener(new NumberPickerListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter--;
                if(counter < 0){
                    counter++;
                    return;
                }
                display.setText("" + counter);
            }
        }));
    }

    private String monthConverter(int month) {
        //converts the month of the year to text
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

    private int getIndex(Spinner spinner, String myString) //used for setting values to spinners
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
        //must be public as it is set as the onClick method for the button in XML
        daySelected = day.getSelectedItem().toString();
        monthSelected = month.getSelectedItem().toString();
        yearSelected = year.getSelectedItem().toString();

        //checks for valid input, invalid set the field to red so they can reinput data
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
            //write the details to the event file
            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_PRIVATE);
                fos.write(v.addHours(toFile).getBytes());
                fos.close();
            } catch (Exception e) {
            }
            submitEvent(view);
        }
        else{
            Toast.makeText(AddHoursRecurringEvent.this, "Please update all red fields",
                        Toast.LENGTH_SHORT).show();
        }
    }

    private void submitEvent(View view) {
        if(v.getCandidPath().equals("No candid photo present"))
            requestCandidPhoto();
        else
            returnToMainActivity();
    }

    private void requestCandidPhoto() {
        DialogFragment g = new ConfirmAddCandidShotDialog();
        g.show(getSupportFragmentManager(), "confirmAddCandidShot");
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

    @Override
    public void onCandidDialogCapture(DialogFragment dialog) { //dialog selection
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CANDID_CAMERA_REQUEST);
    }

    @Override
    public void onCandidDialogSelect(DialogFragment dialog) { //dialog selection
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, CANDID_RESULT_LOAD_IMAGE);
    }

    @Override
    public void onCandidDialogLater(DialogFragment dialog) { //dialog selection
        //path is already set to "No candid photo present" so do nothing, return to main activity
        returnToMainActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when activity resumes after photo is captured or selected from the gallery
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CANDID_CAMERA_REQUEST && resultCode == RESULT_OK) { //photo captured
            candidPhoto = (Bitmap) data.getExtras().get("data");

            saveImageToInternalStorage(candidPhoto, "candid.jpeg");

            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_PRIVATE);
                fos.write((v.setCandidPhotoPath(v.getName() + "candid.jpeg")).getBytes());
                fos.close();
            } catch (Exception e) {
            }

            //toastImage(candidPhoto);

            //requestSignaturePhoto();
            returnToMainActivity();
        }
        else if (requestCode == CANDID_RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) { //photo selected from gallery
            //get the data and then create the bitmap image
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            selectedCandidImagePath = cursor.getString(columnIndex);
            cursor.close();

            try {
                candidPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try { //check if the image is rotated, if it is, rotate it accordingly
                ExifInterface exif = new ExifInterface(MediaStore.Images.Media.DATA);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();
                if(orientation == 6)
                    matrix.postRotate(90);
                else if(orientation == 3)
                    matrix.postRotate(180);
                else if(orientation == 8)
                    matrix.postRotate(270);
                candidPhoto = Bitmap.createBitmap(candidPhoto, 0, 0,
                        candidPhoto.getWidth(), candidPhoto.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            saveImageToInternalStorage(candidPhoto, "candid.jpeg");

            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_PRIVATE);
                fos.write((v.setCandidPhotoPath(v.getName() + "candid.jpeg")).getBytes());
                fos.close();
            } catch (Exception e) {
            }

            //requestSignaturePhoto();
            returnToMainActivity();
            //toastImage(candidPhoto);
        }
        else{
            //path is already set to "No candid photo present" so do nothing, return to main activity
            returnToMainActivity();
            //requestSignaturePhoto();
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmap, String suffix) {
        try{
            FileOutputStream fos = openFileOutput(v.getName() + suffix, MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addPhotoNameToFile(String s) {
            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_APPEND);
                fos.write((s).getBytes());
                fos.close();
            } catch (Exception e) {
            }
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("position_value",0);
        startActivity(intent);
        finish();
    }
}
