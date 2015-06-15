package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;


public class GetSingleActivityInfo extends ActionBarActivity implements ConfirmAddCandidShotDialog.NoticeDialogListener, ConfirmAddSignatureDialog.NoticeDialogListener {

    private static final int CANDID_CAMERA_REQUEST = 12345;
    private static final int CANDID_RESULT_LOAD_IMAGE = 54321;
    private static final int SIGNATURE_CAMERA_REQUEST = 11223;
    private static final int SIGNATURE_RESULT_LOAD_IMAGE = 32211;
    private String selectedCandidImagePath;
    private String selectedSignatureImagePath;
    private String nameStr;
    private Bitmap candidPhoto;
    private Bitmap signaturePhoto;

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

// check for correct user input
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
                            nameStr = (nameTxt.getText()).toString();

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
        requestCandidPhoto();
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
        addPhotoNameToFile("photo has not been added;");
        returnToMainActivity();
        //requestSignaturePhoto();
    }
    //implemented methods associated methods for candid shot dialog///////////////

    ////implemented methods associated methods for signature dialog///////////////
    private void requestSignaturePhoto(){
        DialogFragment f = new ConfirmAddSignatureDialog();
        f.show(getSupportFragmentManager(), "confirmAddSignature");
    }

    @Override
    public void onSignatureDialogCapture(DialogFragment dialog) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, SIGNATURE_CAMERA_REQUEST);
    }

    @Override
    public void onSignatureDialogSelect(DialogFragment dialog) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, SIGNATURE_RESULT_LOAD_IMAGE);
    }

    @Override
    public void onSignatureDialogLater(DialogFragment dialog) {
        addPhotoNameToFile("photo has not been added;");

        returnToMainActivity();
    }
    ////implemented methods associated methods for signature dialog///////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CANDID_CAMERA_REQUEST && resultCode == RESULT_OK) {
            candidPhoto = (Bitmap) data.getExtras().get("data");

            saveImageToInternalStorage(candidPhoto, "candid.jpeg");

            addPhotoNameToFile(nameStr + "candid.jpeg;");

            //toastImage(candidPhoto);

            //requestSignaturePhoto();
            returnToMainActivity();
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

            addPhotoNameToFile(nameStr + "candid.jpeg;");

            //requestSignaturePhoto();
            returnToMainActivity();
            //toastImage(candidPhoto);
        }
        else{
            //save string to file saying there is no photo yet
            addPhotoNameToFile("photo has not been added;");
            returnToMainActivity();
            //requestSignaturePhoto();
        }


        if (requestCode == SIGNATURE_CAMERA_REQUEST && resultCode == RESULT_OK) {
            signaturePhoto = (Bitmap) data.getExtras().get("data");

            saveImageToInternalStorage(signaturePhoto, "signature.jpeg");

            addPhotoNameToFile(nameStr + "signature.jpeg;");

            //toastImage(signaturePhoto);

            returnToMainActivity();
        }
        else if (requestCode == SIGNATURE_RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
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

            saveImageToInternalStorage(signaturePhoto, "signature.jpeg");

            addPhotoNameToFile(nameStr + "signature.jpeg;");

            //toastImage(signaturePhoto);

            returnToMainActivity();
        }
        else{
            //save string to file saying there is no photo yet
            addPhotoNameToFile("photo has not been added;");

            returnToMainActivity();
        }
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("position_value",1);
        startActivity(intent);
        finish();
    }

    private void addPhotoNameToFile(String s) {
        try {
            FileOutputStream fos = openFileOutput(nameStr + ".txt", Context.MODE_APPEND);
            fos.write((s).getBytes());
            fos.close();
        } catch (Exception e) {
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmap, String suffix) {
        try{
            FileOutputStream fos = openFileOutput(nameStr + suffix, MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
// breifly show image for confirmation
    private void toastImage(Bitmap image){
        Toast toast = new Toast(this);
        ImageView view = new ImageView(this);
        view.setImageBitmap(image);
        toast.setView(view);
        toast.show();
    }
}
