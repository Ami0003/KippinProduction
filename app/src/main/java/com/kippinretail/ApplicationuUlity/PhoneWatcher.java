package com.kippinretail.ApplicationuUlity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by kamaljeet.singh on 11/21/2016.
 */

 public class PhoneWatcher implements TextWatcher {

    final char concater = '-';

    EditText editText;

    public PhoneWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String data = s.toString();

        int length = data.length();

        if (length == 4) {
            String pre = data.substring(0, 3);

            int cursor = 0;

            if (!data.endsWith(concater + "")) {
                char post = data.charAt(3);
                setText(pre + concater + post);
            } else {
                setText(pre);
            }
            editText.setSelection(editText.getText().toString().length());
        } else if (length > 4 && data.charAt(3) != concater) {
            data = data.replace(concater + "", "");

            String pre = data.substring(0, 3);

            String post = data.substring(3, data.length());
            int cursor = 0;
            setText(pre + concater + post);
            editText.setSelection(editText.getText().toString().length());
        }

    }

    private void setText(String text) {
        editText.removeTextChangedListener(this);
        editText.setText(text);

        editText.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
