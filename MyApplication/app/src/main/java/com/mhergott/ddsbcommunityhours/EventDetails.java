package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EventDetails extends ActionBarActivity implements RecurringEventInstanceDialog.NoticeDialogListener, ConfirmDeleteDialog.NoticeDialogListener , SubmitDialog.NoticeDialogListener, ConfirmAddCandidShotDialog.NoticeDialogListener {

    private static final int SUBMIT_RESULT_LOAD_IMAGE = 54545;
    private static final int SUBMIT_CAMERA_REQUEST = 45454;
    private static final int CANDID_CAMERA_REQUEST = 21212;
    private static final int CANDID_RESULT_LOAD_IMAGE = 12121;
    String nameTxt;
    String event;
    int position;
    List<String> namesList = new ArrayList<String>();
    ArrayList<String> hoursList;
    boolean recurring = false;
    boolean recurringEmpty = false;
    LinearLayout linearListView;
    VolunteerEvent v;
    static final String STATE_EVENT = "passedEvent";
    static final String STATE_POSITION = "passedPosition";
    private Bitmap signaturePhoto;
    private String selectedSignatureImagePath;
    private Bitmap candidPhoto;
    private String selectedCandidImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        linearListView = (LinearLayout) findViewById(R.id.linear_listview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Event Details");

        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null) { // if there was no bundle passed to the activity, use the saved state
            // Restore value of members from saved state
            nameTxt = savedInstanceState.getString(STATE_EVENT);
            position = savedInstanceState.getInt(STATE_POSITION);
        }
        if(bundle!=null){
            // Use values passed to activity in a bundle
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
        // set up text fields
        TextView name = (TextView) findViewById(R.id.nameTextEventDetails);
        name.setText(v.getName());
        TextView description = (TextView) findViewById(R.id.descriptionTextEventDetails);
        description.setText(v.getDescription());
        TextView organisation = (TextView) findViewById(R.id.organisationTextEventDetails);
        organisation.setText(v.getOrganisation());
        TextView hours = (TextView) findViewById(R.id.hoursTextEventDetails);
        hours.setText(v.getHours());

        ImageView candidPhotoCheckBox = (ImageView) findViewById(R.id.candidPhotoCheckBox);
        if(v.getCandidPath().equals("No candid photo present"))
            candidPhotoCheckBox.setImageResource(android.R.drawable.checkbox_off_background);
        else
            candidPhotoCheckBox.setImageResource(android.R.drawable.checkbox_on_background);

        if(v.getHours().equals("0")){
            recurringEmpty = true;
        }
        //check if there are any recurring events listed in the volunteer event passed in
        if(v.getHoursList()!=null) {
            hoursList = v.getHoursList();
            recurring = true;
            TextView eventDatesAndHoursText = (TextView) findViewById(R.id.eventDatesAndHoursText);
            if(v.getHoursList().size()>0)
                eventDatesAndHoursText.setVisibility(View.VISIBLE);

            for (int i = 0; i < hoursList.size(); i++) {
                //inflate items add items in linear layout instead of listview
                LayoutInflater inflater = null;
                inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView = inflater.inflate(R.layout.recurring_hours_row, null);
                TextView dateView = (TextView) mLinearView.findViewById(R.id.recurringDate);
                TextView hoursView = (TextView) mLinearView.findViewById(R.id.recurringHours);

                mLinearView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                linearListView.addView(mLinearView);

                //set item into row
                String str = hoursList.get(i);
                final String toBeDeleted = str;
                final String dateText = str.substring(0, str.indexOf(';'));
                str = str.substring(str.indexOf(';') + 1);
                if(str.indexOf(';')==-1){
                    return;
                }
                final String hoursText = str.substring(0, str.indexOf(';'));

                dateView.setText(dateText);
                dateView.setTextColor(Color.BLACK);
                hoursView.setText(hoursText);
                hoursView.setTextColor(Color.BLACK);

                //get item row on click
                mLinearView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewRecurringEventInstance(dateText, hoursText, toBeDeleted);
                    }
                });
            }
        }

        LinearLayout candidPhotoLinearLayout = (LinearLayout) findViewById(R.id.candidPhotoLinearLayout);
        candidPhotoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candidPhotoClick();
            }
        });

        Toast.makeText(getApplicationContext(), "Click envelope to submit event",
                Toast.LENGTH_LONG).show();
    }

    private void candidPhotoClick() {
        if(v.getCandidPath().equals("No candid photo present")){
            requestCandidPhoto();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the event and position passed into the activity
        savedInstanceState.putString(STATE_EVENT, nameTxt);
        savedInstanceState.putInt(STATE_POSITION, position);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu only if it is recurring
        if(recurring)
            getMenuInflater().inflate(R.menu.menu_event_details_recurring, menu);
        else if(recurringEmpty)
            getMenuInflater().inflate(R.menu.menu_event_details_recurring_empty, menu);
        else
            getMenuInflater().inflate(R.menu.menu_event_details_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: //for up navigation
                Intent i = new Intent(this,MainActivity.class);
                i.putExtra("position_value",position);
                startActivity(i);
                return true;
            case R.id.menu_event_details_add_hours:
                Intent j = new Intent(this,AddHoursRecurringEvent.class);
                j.putExtra("event_details",event);
                startActivity(j);
                return true;
            case R.id.menu_event_details_delete:
                confirmDelete();
                return true;
            case R.id.menu_event_details_submit_single:
                submit();
                return true;
            case R.id.menu_event_details_submit_recurring:
                submit();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    //implemented methods and associated methods for candid shot dialog///////////////
    private void requestCandidPhoto() {
        DialogFragment g = new ConfirmAddCandidShotDialog();
        g.show(getSupportFragmentManager(), "confirmAddCandidShot");
    }

    @Override
    public void onCandidDialogCapture(DialogFragment dialog) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CANDID_CAMERA_REQUEST);
    }

    @Override
    public void onCandidDialogSelect(DialogFragment dialog) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, CANDID_RESULT_LOAD_IMAGE);
    }

    @Override
    public void onCandidDialogLater(DialogFragment dialog) {
        //path is already set to "No candid photo present" so do nothing
    }
    //implemented methods associated methods for candid shot dialog///////////////

// open up dialog to enable user to submit
    private void submit() {
        DialogFragment f = new SubmitDialog();
        f.show(getSupportFragmentManager(), "submit");
    }

    private void ViewRecurringEventInstance(String date, String hours, String toDelete){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("recurring_event_instance_date", date);
        editor.putString("recurring_event_instance_hours", hours);
        editor.putString("recurring_event_instance_toDelete", toDelete);
        editor.commit();

        DialogFragment f = new RecurringEventInstanceDialog();
        f.show(getSupportFragmentManager(), "recurringEventInstance");
    }
    @Override
    public void onDialogDelete(DialogFragment dialog) {
        confirmDelete();
        return;
    }
    @Override
    public void onDialogCancel(DialogFragment dialog) {
        return;
    }

    private void confirmDelete(){
        DialogFragment g = new ConfirmDeleteDialog();
        g.show(getSupportFragmentManager(), "confirmDelete");
    }
    @Override
    public void onDialogDeleteConfirm(DialogFragment dialog) {
        if(recurring) {
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            String toDelete = sharedPref.getString("recurring_event_instance_toDelete", "");

            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_PRIVATE);
                fos.write(v.removeHours(toDelete).getBytes());
                fos.close();
            } catch (Exception e) {
            }

            finish();
            Intent intent = new Intent(this, EventDetails.class);
            intent.putExtra("event_name", v.getName());
            intent.putExtra("tab", 0);
            startActivity(intent);
        }
        else {
            FileInputStream fis;
            String currentNames = "";
            try {
                fis = openFileInput("CompletedNames.txt");
                byte[] input = new byte[fis.available()];
                while (fis.read(input) != -1) {
                }
                currentNames = new String(input, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            listCreator(currentNames);

            //remove the event from the list, make the list a string again, then resave the list string
            namesList.remove(v.getName());
            String toFile = "";
            for (int a = 0; a < namesList.size(); a++) {
                toFile = toFile + namesList.get(a) + ";";
            }

            if (toFile.equals("")) {
                deleteFile("CompletedNames.txt");
            }
            else {
                try {
                    FileOutputStream fos = openFileOutput("CompletedNames.txt", Context.MODE_PRIVATE);
                    fos.write(toFile.getBytes());
                    fos.close();
                } catch (Exception e) {
                }
            }

            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("position_value",1);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onDialogCancelConfirm(DialogFragment dialog) {
        return;
    }

    private void listCreator(String str){
        while(str.indexOf(';')!=-1){
            namesList.add(str.substring(0, str.indexOf(';')));
            str = str.substring(str.indexOf(';') + 1);
        }
    }

    @Override
    public void onSubmitDialogCapture(DialogFragment dialog) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, SUBMIT_CAMERA_REQUEST);
    }

    @Override
    public void onSubmitDialogSelect(DialogFragment dialog) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, SUBMIT_RESULT_LOAD_IMAGE);
    }
    @Override
    public void onSubmitDialogCancel(DialogFragment dialog){
        Toast.makeText(getApplicationContext(), "No photo was selected, try again", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CANDID_CAMERA_REQUEST && resultCode == RESULT_OK) {
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
            //returnToMainActivity();
        }
        else if (requestCode == CANDID_RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
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

            saveImageToInternalStorage(candidPhoto, "candid.jpeg");

            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_PRIVATE);
                fos.write((v.setCandidPhotoPath(v.getName() + "candid.jpeg")).getBytes());
                fos.close();
            } catch (Exception e) {
            }

            //requestSignaturePhoto();
            //returnToMainActivity();
            //toastImage(candidPhoto);
            Intent intent = new Intent(this,EventDetails.class);
            intent.putExtra("event_name",v.getName());
            intent.putExtra("tab",0);
            startActivity(intent);
        }

        if (requestCode == SUBMIT_CAMERA_REQUEST && resultCode == RESULT_OK) {
            signaturePhoto = (Bitmap) data.getExtras().get("data");

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
                signaturePhoto = Bitmap.createBitmap(signaturePhoto, 0, 0,
                        signaturePhoto.getWidth(), signaturePhoto.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            saveImageToInternalStorage(signaturePhoto, "signature.jpeg");

            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_PRIVATE);
                fos.write(v.setSignaturePhotoPath(v.getName() + "signature.jpeg").getBytes());
                fos.close();
            } catch (Exception e) {
            }

            //toastImage(candidPhoto);

            //requestSignaturePhoto();
            toEmailActivity();
            finish();
        }
        else if (requestCode == SUBMIT_RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            selectedSignatureImagePath = cursor.getString(columnIndex);
            cursor.close();

            try {
                signaturePhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try { //check if the image is rotated, if it is, rotate it accordingly
                ExifInterface exif = new ExifInterface(selectedSignatureImagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Matrix matrix = new Matrix();
                if(orientation == 6)
                    matrix.postRotate(90);
                else if(orientation == 3)
                    matrix.postRotate(180);
                else if(orientation == 8)
                    matrix.postRotate(270);
                signaturePhoto = Bitmap.createBitmap(signaturePhoto, 0, 0,
                        signaturePhoto.getWidth(), signaturePhoto.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            saveImageToInternalStorage(signaturePhoto, "signature.jpeg");

            try {
                FileOutputStream fos = openFileOutput(v.getName() + ".txt", Context.MODE_PRIVATE);
                fos.write(v.setSignaturePhotoPath(v.getName() + "signature.jpeg").getBytes());
                fos.close();
            } catch (Exception e) {
            }

            //requestSignaturePhoto();
            toEmailActivity();
            finish();
            //toastImage(candidPhoto);
        }
        else{
            Toast.makeText(this, "No photo was selected, try again", Toast.LENGTH_LONG).show();
        }
    }
// send info to another activity for it to be emailed
    private void toEmailActivity() {
        Intent intent = new Intent(this,EmailActivity.class);
        intent.putExtra("name", v.getName());
        intent.putExtra("description", v.getDescription());
        intent.putExtra("organisation", v.getOrganisation());
        intent.putExtra("hours", v.getHours());
        intent.putExtra("signature", v.getName()+"signature.jpeg");
        intent.putExtra("candid", v.getName()+"candid.jpeg");
        startActivity(intent);
        finish();
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

}
