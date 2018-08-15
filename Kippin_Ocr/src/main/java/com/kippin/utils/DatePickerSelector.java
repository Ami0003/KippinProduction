package com.kippin.utils;

import android.app.Activity;
import android.content.Context;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by gaganpreet.singh on 4/1/2016.
 */
public class DatePickerSelector {

    private int year = -1, monthOfYear = -1, dayOfMonth = -1;
    public Calendar newCalendar = Calendar.getInstance();
    private onDatePickerSelector onDatePickerSelector;
    private Context context;

    public DatePickerSelector(Context context, final onDatePickerSelector onDatePickerSelector) {
        year = newCalendar.get(Calendar.YEAR);
        monthOfYear = newCalendar.get(Calendar.MONTH);
        dayOfMonth = newCalendar.get(Calendar.DAY_OF_MONTH);
        this.onDatePickerSelector = onDatePickerSelector;
        this.context = context;
    }

    public void show(final int textId) {
        DatePickerDialog datePickerDialog;datePickerDialog= new DatePickerDialog(context, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DatePickerSelector.this.year = year;
                DatePickerSelector.this.monthOfYear = monthOfYear;
                DatePickerSelector.this.dayOfMonth = dayOfMonth;
                onDatePickerSelector.onDateSelected(textId , year, monthOfYear, dayOfMonth);
            }
        }, year, monthOfYear, dayOfMonth);

        datePickerDialog.show();
    }


    public interface onDatePickerSelector {
        public void onDateSelected(int textId ,int year, int monthOfYear, int dayOfMonth);
    }
}
