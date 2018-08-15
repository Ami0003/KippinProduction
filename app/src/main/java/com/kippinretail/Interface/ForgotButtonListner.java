package com.kippinretail.Interface;

import android.app.Dialog;
import android.widget.EditText;

/**
 * Created by kamaljeet.singh on 11/16/2016.
 */

public interface ForgotButtonListner {

    public void yes(String s, Dialog dialog, EditText editTextEmail);
    public void no();

}
