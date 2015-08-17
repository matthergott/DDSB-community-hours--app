package com.mhergott.ddsbcommunityhours;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecurringEventFrag extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_DATE = "date_param";
    private static final String ARG_HOURS = "hours_param";

    private OnFragmentInteractionListener mListener;

    /**
     * Use this method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static RecurringEventFrag newInstance(String param1, String param2) {
        RecurringEventFrag fragment = new RecurringEventFrag();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, param1);
        args.putString(ARG_HOURS, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RecurringEventFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String dateParam = getArguments().getString(ARG_DATE);
            String hoursParam = getArguments().getString(ARG_HOURS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recurring_event, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
