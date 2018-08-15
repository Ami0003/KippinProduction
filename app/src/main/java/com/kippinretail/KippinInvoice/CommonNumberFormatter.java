package com.kippinretail.KippinInvoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Karan Nassa on 20-07-2017.
 * <p>
 * Organization : ucreate.it
 * Email        : karan@ucreate.it
 */
public class CommonNumberFormatter {
    public static String strOutputPattern = "####.00";
    public static DecimalFormat decimalFormat = new DecimalFormat(strOutputPattern);

    //  public static NumberFormat format = NumberFormat.getCurrencyInstance();

    public static DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    public static String getRound(float totalAmount) {
        String dx;
        if(String.valueOf(totalAmount).equals("0.0")){
            dx = "0.00";
            return dx;
        }
        if(String.valueOf(totalAmount).equals("0")){
            dx = "0.00";
            return dx;
        }
        if(String.valueOf(totalAmount).equals(".0")){
            dx = "0.00";
            return dx;
        }

        if (!String.valueOf(totalAmount).equals("")) {
            DecimalFormat df = new DecimalFormat("#,###.00");
             dx = df.format(totalAmount);
        } else {
            dx = "0.00";
        }
        return dx.replace(",","");
    }
    public static double roundBigDecimal(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static String getRoundSimple(float totalAmount) {
        //Log.e("get---Round----Simple:", "" + totalAmount);
        String dx;
        if(String.valueOf(totalAmount).equals("0.0")){
            dx = "0.00";
            return dx;
        }
        if(String.valueOf(totalAmount).equals("0")){
            dx = "0.00";
            return dx;
        }
        if(String.valueOf(totalAmount).equals(".0")){
            dx = "0.00";
            return dx;
        }
        if (!String.valueOf(totalAmount).equals("")) {
            DecimalFormat df = new DecimalFormat("##.00");
            dx = df.format(totalAmount);
        } else {
            dx = "0.00";
        }
        double dx_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(dx), 2);
        dx=""+dx_double;
        //Log.e("dx:",""+dx);
        return dx;
    }

    public static String getCurrencyFormat(Double strInputNumber) {
        String formattedResult = "";
        try {
            formattedResult = NumberFormat.getNumberInstance(Locale.getDefault()).format(strInputNumber);
            // formattedResult =  format.format(strInputNumber);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return formattedResult;
    }

}
