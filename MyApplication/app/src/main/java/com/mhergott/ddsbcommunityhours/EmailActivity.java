package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


public class EmailActivity extends ActionBarActivity {

    private static final int EMAIL_REQUEST = 44444;
    String name;
    String description;
    String organisation;
    String hours;
    private String personalInformation;
    private String userDOB;
    private String userName;
    private String userSchool;
    private String signatureFileName;
    private Bitmap signaturePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        //get key values from bundle passed to activity
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        description = bundle.getString("description");
        organisation = bundle.getString("organisation");
        hours = bundle.getString("hours");
        signatureFileName = bundle.getString("signature");

        //get signature photo bitmap from internal storage
        File file = getBaseContext().getFileStreamPath(signatureFileName);
        try {
            signaturePic = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        getPersonalInformation();

        //save the form from drawable to internal storage to be accessed later
        //write all the text to the form before sending it in the email
        saveImageToInternalStorage(writeTextOnDrawable(R.drawable.hours_form,"yolo",this));

        //commence the intent to start sending the email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"matthewhergott@sympatico.ca"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "text");

        intent.setType("image/jpeg");
        File form = getFileStreamPath("hoursForm.jpeg");
        Uri uri = Uri.fromFile(form);

        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION //to make sure gmail can read it
        );

        startActivityForResult(intent, EMAIL_REQUEST);

        finish();
    }

    private void getPersonalInformation() {
        //get all personal information from internal storage and treat the text
        FileInputStream fis;
        try {
            fis = openFileInput("personalInfo.txt");
            byte[] input = new byte[fis.available()];
            while (fis.read(input) != -1) {}
            personalInformation = new String(input, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        userName = personalInformation.substring(0, personalInformation.indexOf(";"));
        personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
        userSchool = personalInformation.substring(0, personalInformation.indexOf(";"));
        personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
        userDOB = personalInformation.substring(0, personalInformation.indexOf(";"));
        personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
        userDOB += " " + personalInformation.substring(0, personalInformation.indexOf(";"));
        personalInformation = personalInformation.substring(personalInformation.indexOf(";")+1);
        userDOB += " " + personalInformation.substring(0, personalInformation.indexOf(";"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EMAIL_REQUEST && resultCode == RESULT_OK) {
            finish();
        }
    }
// method to overlay information from user on to volunteer hours form
    private Bitmap writeTextOnDrawable(int drawableId, String text, Context mContext) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(mContext, 13));

        //Rect textRect = new Rect();
        //paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        canvas.drawText(userName,225,160,paint);
        canvas.drawText(userDOB,1000,160,paint);
        canvas.drawText(userSchool,220,200,paint);
        canvas.drawText(name,100,300,paint);
        canvas.drawText(description,100,360,paint);
        canvas.drawText(organisation,615,300,paint);
        canvas.drawText(hours,1330,340,paint);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(signaturePic, 100, 100, false);
        canvas.drawBitmap(resizedBitmap, 100, 100, paint);
        /*
        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(mContext, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);
        */

        return bm;
        //return new BitmapDrawable(getResources(), bm);
    }

    public static int convertToPixels(Context context, int nDP)
    {
        //used in the writeTextToDrawable method above
        final float conversionScale = context.getResources().getDisplayMetrics().density;
        return (int) ((nDP * conversionScale) + 0.5f) ;
    }
// same the image for later reference
    public boolean saveImageToInternalStorage(Bitmap image) {
        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = openFileOutput("hoursForm.jpeg", Context.MODE_WORLD_READABLE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email, menu);
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
}
