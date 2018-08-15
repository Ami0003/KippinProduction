package com.kippin.connectedbankacc;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.lang.reflect.Method;

//com.mitosoft.ui.widgets.NoDefaultSpinner
public class CustomSpinner extends Spinner {

    private int lastSelected = 0;
    private static Method s_pSelectionChangedMethod = null;


    static {
        try {
            Class noparams[] = {};
            Class targetClass = AdapterView.class;

            s_pSelectionChangedMethod = targetClass.getDeclaredMethod("selectionChanged", noparams);
            if (s_pSelectionChangedMethod != null) {
                s_pSelectionChangedMethod.setAccessible(true);
            }

        } catch( Exception e ) {
            Log.e("Custom spinner bug:", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void testReflectionForSelectionChanged() {
//        try {
//            Class noparams[] = {};
//            s_pSelectionChangedMethod.invoke(this, noparams);
//        } catch (Exception e) {
//            Log.e("custom spinner bug", e.getMessage());
//            e.printStackTrace();
//        }
    }




    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        if(lastSelected == which)
            testReflectionForSelectionChanged();

        lastSelected = which;
    }

    @Override
    public void setSelection(int position)
    {
        super.setSelection(position);

        if (position == getSelectedItemPosition()  && listener!=null)
        {
            listener.onItemSelected(null, null, position, 0);
        }

    }


    public void setOnItemSelectedListener(OnItemSelectedListener listener)
    {
        this.listener = listener;
    }


    OnItemSelectedListener listener;

}