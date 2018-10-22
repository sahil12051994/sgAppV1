package com.swasthgarbh.root.swasthgarbh;

public class MedicineListClass {

    private String medName, period, comments, startDate;

    public MedicineListClass (String medName, String period, String comments, String startDate){

        this.medName = medName;
        this.period = period;
        this.comments = comments;
        this.startDate = startDate;

    }

    public String getMedName(){ return medName; }

}
