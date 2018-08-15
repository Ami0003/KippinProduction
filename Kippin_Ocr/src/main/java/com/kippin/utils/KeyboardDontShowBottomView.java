package com.kippin.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by dilip.singh on 1/28/2016.
 */
public class KeyboardDontShowBottomView {

    Context mContext;
    Activity mActivity;
    RelativeLayout mParentLayout;
    LinearLayout mChildLayout;


    /**
     * Constructor for call visible and gone bottom child layout
     * @param context
     * @param parentLayout
     * @param childLayout
     */
    public KeyboardDontShowBottomView(Context context,RelativeLayout parentLayout,LinearLayout childLayout){

        mContext = context;
        mActivity = (Activity)mContext;
        mParentLayout = parentLayout;
        mChildLayout = childLayout;


        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if ((mParentLayout.getRootView().getHeight() - mParentLayout.getHeight()) >
                        mParentLayout.getRootView().getHeight()/3) {

                    mChildLayout.setVisibility(View.GONE);

                } else {

                    mChildLayout.setVisibility(View.VISIBLE);

                }
            }
        });

    }

}
