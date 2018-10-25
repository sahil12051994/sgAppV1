package com.swasthgarbh.root.swasthgarbh;

public class PatientImageRow {

    public String date_date, date_month, date_year, pk;


    PatientImageRow(String date, String pk) {
        this.date_date = get_date_date(date);
        this.date_month = get_date_month(date);
        this.date_year = get_date_year(date);
        this.pk = pk;
    }

    private String get_date_year(String date) {
        return date.split("-")[0];
    }

    private String get_date_month(String date) {
        String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int mon_int = Integer.parseInt(date.split("-")[1]);
        return months[mon_int];
    }

    private String get_date_date(String date) {
        return date.split("-")[2];
    }
}
