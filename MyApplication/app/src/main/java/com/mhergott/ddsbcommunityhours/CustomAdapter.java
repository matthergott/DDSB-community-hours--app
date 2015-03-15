package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {

    CustomAdapter(Context context, String[] foods) {
        super(context, R.layout.custom_row, foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mattsInflater = LayoutInflater.from(getContext());
        View customView = mattsInflater.inflate(R.layout.custom_row, parent, false);

        TextView nameText = (TextView) customView.findViewById(R.id.nameText);
        TextView descriptionText = (TextView) customView.findViewById(R.id.descriptionText);
        TextView organizationText = (TextView) customView.findViewById(R.id.organisationText);
        TextView hoursText = (TextView) customView.findViewById(R.id.hoursText);

        //add something to set the text to
        VolunteerEvent v = new VolunteerEvent;
        nameText.setText();
        descriptionText.setText();
        organizationText.setText();
        hoursText.setText();

        return customView;
    }
}
