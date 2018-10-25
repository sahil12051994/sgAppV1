package com.swasthgarbh.root.swasthgarbh;

import java.util.Date;

public class MedicineListClass {

    private String medName, comments, startDate, endDate, frequency;

    public MedicineListClass (String medName, String startDate, String endDate, String freq, String comments){

        this.medName = medName;
        this.frequency = freq;
        this.comments = comments;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public String getMedName(){ return medName; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getComments() { return comments; }
    public String getfreq() { return frequency; }
}
