package com.kippinretail.KippinInvoice;

/**
 * Created by amit on 9/11/2017.
 */

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;


public class DecimalFilter implements TextWatcher {

    int count = -1;
    EditText et;
    Activity activity;

    public DecimalFilter(EditText edittext, Activity activity) {
        et = edittext;
        this.activity = activity;
    }

    public void afterTextChanged(Editable s) {


        if (s.length() > 0) {
            String str = et.getText().toString();
            et.setOnKeyListener(new OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        count--;
                        InputFilter[] fArray = new InputFilter[1];
                        fArray[0] = new InputFilter.LengthFilter(1000000);//Re sets the maxLength of edittext to 1000000.
                        et.setFilters(fArray);
                    }
                    if (count > 2) {
                        //Toast.makeText(activity, "Sorry! You cant enter more than two digits after decimal point!", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
            try {
                if (str.length() > 0) {


                    Log.e("str:", "" + str);
                    Log.e("length:", "" + s.length());
                    char t = str.charAt(s.length() - 1);
                    if (t == '.') {
                        count = 0;
                    }

                    Log.e("-----count-----:", "" + count);
                    if (count >= 0) {
                        if (count == 5) {
                            InputFilter[] fArray = new InputFilter[1];
                            fArray[0] = new InputFilter.LengthFilter(s.length());
                            et.setFilters(fArray); // sets edittext's maxLength to number of digits now entered.
                        }
                        count++;
                    } else {
                        if (count == -1) {
                            count = 0;
                            if(et.getText().toString().equals("0.00")|| et.getText().toString().equals("0.0")){
                                et.setText("");
                            }

                        }
                    }
                }

            } catch (ArrayIndexOutOfBoundsException e) {

            }

        }

    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
// TODO Auto-generated method stub
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
// TODO Auto-generated method stub
    }

}
