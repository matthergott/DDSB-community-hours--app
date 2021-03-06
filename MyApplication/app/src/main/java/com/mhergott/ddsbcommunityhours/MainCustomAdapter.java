package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class MainCustomAdapter extends ArrayAdapter<VolunteerEvent> {
    private static final String TAG = "mattsMessage";
    private Bitmap candidPic;


    public VolunteerEvent[] eventsArr;

    public MainCustomAdapter(Context context, VolunteerEvent[] s) {
        super(context, R.layout.activity_main_custom_row, s);
        eventsArr = s;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.activity_main_custom_row, parent, false);

        TextView nameText = (TextView) customView.findViewById(R.id.nameText);
       // TextView descriptionText = (TextView) customView.findViewById(R.id.descriptionText);
        TextView organizationText = (TextView) customView.findViewById(R.id.organisationText);
        TextView hoursText = (TextView) customView.findViewById(R.id.hoursText);
        ImageView imageView = (ImageView) customView.findViewById(R.id.imageBox);

        //add something to set the text to
        VolunteerEvent v = eventsArr[position];
        nameText.setText(v.getName());
       // descriptionText.setText(v.getDescription());
        organizationText.setText(v.getOrganisation());
        hoursText.setText(v.getHours());
        File candidImageFile = getContext().getFileStreamPath(v.getCandidPath());

        try {
            candidPic = BitmapFactory.decodeStream(new FileInputStream(candidImageFile));
            imageView.setImageBitmap(candidPic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return customView;
    }
}
