package com.mhergott.ddsbcommunityhours;

import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;



public class VolunteerEvent extends ActionBarActivity {
    private String timeStamp;
    private String signaturePath;
    private String candidPath;
    private String submitted;
    private String name;
    private String description;
    private String organisation;
    private String supervisorName;
    private String telephoneNumber;
    private String hours;
    private ArrayList<String> hoursList = new ArrayList<>();
 
// initialize activity based on internal data
    public VolunteerEvent(String str) {
        timeStamp = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        candidPath = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        signaturePath = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        submitted = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        name = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        description = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        organisation = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        supervisorName = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        telephoneNumber = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        hours = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
    }

    public VolunteerEvent(String str,int i) {
        timeStamp = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        candidPath = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        signaturePath = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        submitted = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        name = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        description = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        organisation = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        supervisorName = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        telephoneNumber = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        hours = str.substring(0, str.indexOf(';'));
        str = str.substring(str.indexOf(';') + 1);
        boolean recurring;
        recurring = i == 0;
        if(recurring){
            while(str.indexOf(';')!=-1){
                String toAdd = str.substring(0, str.indexOf(';')+1);
                str = str.substring(str.indexOf(';')+1);
                toAdd = toAdd + str.substring(0, str.indexOf(';')+1);
                str = str.substring(str.indexOf(';') + 1);
                //day month year;hours;
                hoursList.add(toAdd);
            }
        }
        else
            hoursList = null;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    public String setCandidPhotoPath(String s){
        candidPath = s;
        String toFile = timeStamp + ";" + candidPath + ";" + signaturePath + ";" + submitted + ";" +
                name + ";" +description + ";" + organisation + ";" + supervisorName + ";" +
                telephoneNumber + ";" + hours + ";";
        if(hoursList!=null){
            for (int a = 0; a < hoursList.size(); a++){
                toFile = toFile + hoursList.get(a);
            }
        }
        return toFile;
    }
    public String setSignaturePhotoPath(String s){
        signaturePath = s;
        String toFile = timeStamp + ";" + candidPath + ";" + signaturePath + ";" + submitted + ";" +
                name + ";" +description + ";" + organisation + ";" + supervisorName + ";" +
                telephoneNumber + ";" + hours + ";";
        if(hoursList!=null){
            for (int a = 0; a < hoursList.size(); a++){
                toFile = toFile + hoursList.get(a);
            }
        }
        return toFile;
    }
    public String getCandidPath() {
        return candidPath;
    }
    public String getSignaturePath() {
        return signaturePath;
    }
    public boolean isSubmitted(){
        return !submitted.equals("Not submitted");
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getOrganisation(){
        return organisation;
    }
    public String getSupervisorName() {
        return supervisorName;
    }
    public String getTelephoneNumber() {
        return telephoneNumber;
    }
    public String getHours(){
        return hours;
    }
    public String addHours(String string){ //recreate the entire string of the volunteer event to be resaved
        hoursList.add(string);
        string = string.substring(string.indexOf(';') + 1);
        int temp = Integer.valueOf(hours);
        temp += Integer.valueOf(string.substring(0,string.indexOf(';')));
        hours = String.valueOf(temp);

        String toFile = timeStamp + ";" + candidPath + ";" + signaturePath + ";" + submitted + ";" +
                name + ";" +description + ";" + organisation + ";" + supervisorName + ";" +
                telephoneNumber + ";" + hours + ";";
        if(hoursList!=null){
            for (int a = 0; a < hoursList.size(); a++){
                toFile = toFile + hoursList.get(a);
            }
        }

        return toFile;
    }
    public String removeHours(String string){ //recreate the entire string of the volunteer event to be resaved
        hoursList.remove(string);
        string = string.substring(string.indexOf(';') + 1);
        int temp = Integer.valueOf(hours);
        temp -= Integer.valueOf(string.substring(0,string.indexOf(';')));
        hours = String.valueOf(temp);

        String toFile = timeStamp + ";" + candidPath + ";" + signaturePath + ";" + submitted + ";" +
                name + ";" +description + ";" + organisation + ";" + supervisorName + ";" +
                telephoneNumber + ";" + hours + ";";
        if(hoursList!=null){
            for (int a = 0; a < hoursList.size(); a++){
                toFile = toFile + hoursList.get(a);
            }
        }

        return toFile;
    }
    public ArrayList<String> getHoursList(){
        return hoursList;
    }

}

