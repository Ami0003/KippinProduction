package com.kippin.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by gaganpreet.singh on 4/11/2016.
 */
public class CustomSpinner extends Spinner {
    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CustomSpinner(Context context) {
        super(context);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
