package com.kippinretail.ApplicationuUlity;

import android.widget.EditText;

import java.util.regex.Pattern;

public class Validation {

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d^{7}";

    // Error Messages
    private static final String REQUIRED_MSG = "This field can't be blank";
    private static final String EMAIL_MSG = "Please enter valid email";
    private static final String PHONE_MSG = "phone";

    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }


    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex,
                                  String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasText(editText))
            return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }
        ;

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    // Check password and confirm password
    public static boolean checkPassWordAndConfirmPassword(String password, String confirmPassword) {
        boolean pstatus = false;
        if (confirmPassword != null && password != null) {
            if (password.equals(confirmPassword)) {
                pstatus = true;
            }
        }
        return pstatus;
    }

    public static boolean validateCardNo(String cardNo)
    {
        if(cardNo.length()==16){
            char[] c = cardNo.toCharArray();
            int[] cint = new int[16];
            for(int i=0;i<16;i++){
                if(i%2==1){
                    cint[i] = Integer.parseInt(String.valueOf(c[i]))*2;
                    if(cint[i] >9)
                        cint[i]=1+cint[i]%10;
                }
                else
                    cint[i] = Integer.parseInt(String.valueOf(c[i]));
            }
            int sum=0;
            for(int i=0;i<16;i++){
                sum+=cint[i];
            }
            if(sum%10==0)
                return true;

            else
                return false;

        }else
            return false;

    }
}