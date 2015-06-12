package com.mhergott.ddsbcommunityhours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class MainCustomAdapter extends ArrayAdapter<VolunteerEvent> {
    private static final String TAG = "mattsMessage";

    public VolunteerEvent[] eventsArr;

    public MainCustomAdapter(Context context, VolunteerEvent[] s) {
        super(context, R.layout.activity_main_custom_row, s);
        eventsArr = s;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mattsInflater = LayoutInflater.from(getContext());
        View customView = mattsInflater.inflate(R.layout.activity_main_custom_row, parent, false);

        TextView nameText = (TextView) customView.findViewById(R.id.nameText);
        TextView descriptionText = (TextView) customView.findViewById(R.id.descriptionText);
        TextView organizationText = (TextView) customView.findViewById(R.id.organisationText);
        TextView hoursText = (TextView) customView.findViewById(R.id.hoursText);


        //add something to set the text to
        VolunteerEvent v = eventsArr[position];
        nameText.setText(v.getName());
        descriptionText.setText(v.getDescription());
        organizationText.setText(v.getOrganisation());
        hoursText.setText(v.getHours());

        return customView;
    }
}
