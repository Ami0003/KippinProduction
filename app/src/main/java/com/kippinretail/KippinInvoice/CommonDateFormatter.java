package com.kippinretail.KippinInvoice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Karan Nassa on 20-07-2017.
 * <p>
 * Organization : ucreate.it
 * Email        : karan@ucreate.it
 */
public class CommonDateFormatter {

    /**
     * Implements a method to RETURN the name of MONTH from
     * specific date format.
     */
    public static String getFormatDate(String strDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date date = inputFormat.parse(strDate);
            strDate = outputFormat.format(date);
        } catch (ParseException exp) {
            exp.printStackTrace();
        }
        return strDate;
    }

    /**
     * Implements a method to RETURN the name of MONTH from
     * specific date format.
     */
    public static String getFormatDateHiphen(String strDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date date = inputFormat.parse(strDate);
            strDate = outputFormat.format(date);
        } catch (ParseException exp) {
            exp.printStackTrace();
        }
        return strDate;
    }

    public static String formatDate(String strDate){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(strDate);
        return formattedDate;
    }
}
