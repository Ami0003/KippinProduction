package com.kippinretail.gesture;

import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;

/**
 * Created by sandeep.singh on 10/27/2016.
 */
public class RotationGestureDetector {

    private static final int INVALID_POINTER_ID = -1;
    private PointF mFPoint = new PointF();
    private PointF mSPoint = new PointF();
    private int mPtrID1, mPtrID2;
    private float mAngle;
    private View mView;
    private float prevAngle = 0.0f;
    private float lastUpdatedPrevAngle = 0.0f;
    private OnRotationGestureListener mListener;
    double oldD=1;
    private String mode=null;
    public float getAngle() {
        return mAngle;
    }
    public RotationGestureDetector(OnRotationGestureListener listener) {
        mListener = listener;

        mPtrID1 = INVALID_POINTER_ID;
        mPtrID2 = INVALID_POINTER_ID;
    }

    public RotationGestureDetector(OnRotationGestureListener listener, View v) {
        mListener = listener;
        mView = v;
        mPtrID1 = INVALID_POINTER_ID;
        mPtrID2 = INVALID_POINTER_ID;
    }

    public boolean onTouchEvent(MotionEvent event){


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_OUTSIDE:

                break;
            case MotionEvent.ACTION_DOWN:

                mPtrID1 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:


                mPtrID2 = event.getPointerId(event.getActionIndex());
                getRawPoint(event, mPtrID1, mSPoint);
                getRawPoint(event, mPtrID2, mFPoint);

                break;
            case MotionEvent.ACTION_MOVE:
                if (mPtrID1 != INVALID_POINTER_ID && mPtrID2 != INVALID_POINTER_ID){
                    PointF nfPoint = new PointF();
                    PointF nsPoint = new PointF();

                    getRawPoint(event, mPtrID1, nsPoint);
                    getRawPoint(event, mPtrID2, nfPoint);

                    mAngle = angleBetweenLines(mFPoint, mSPoint, nfPoint, nsPoint);

                    if (mListener != null) {

                            mListener.onRotation(this,lastUpdatedPrevAngle);
                     //   }


                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mPtrID1 = INVALID_POINTER_ID;
                if(prevAngle>=5.0 || prevAngle<=-5.0 ){
                    lastUpdatedPrevAngle = prevAngle;
                }

                Log.e("====prevAngle====="+prevAngle+ mView.getRotation(),"======lastUpdatedPrevAngle ======="+lastUpdatedPrevAngle);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mPtrID2 = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_CANCEL:
                mPtrID1 = INVALID_POINTER_ID;
                mPtrID2 = INVALID_POINTER_ID;
                break;
            default:
                break;
        }
        return true;
    }

    void getRawPoint(MotionEvent ev, int index, PointF point){
        final int[] location = { 0, 0 };
        mView.getLocationOnScreen(location);

        float x = ev.getX(index);
        float y = ev.getY(index);

        double angle = Math.toDegrees(Math.atan2(y, x));
        angle += mView.getRotation();

        final float length = PointF.length(x, y);

        x = (float) (length * Math.cos(Math.toRadians(angle))) + location[0];
        y = (float) (length * Math.sin(Math.toRadians(angle))) + location[1];

        point.set(x, y);
    }

    private float angleBetweenLines(PointF fPoint, PointF sPoint, PointF nFpoint, PointF nSpoint)
    {
        float angle1 = (float) Math.atan2((fPoint.y - sPoint.y), (fPoint.x - sPoint.x));
        float angle2 = (float) Math.atan2((nFpoint.y - nSpoint.y), (nFpoint.x - nSpoint.x));

        float angle = ((float) Math.toDegrees(angle1 - angle2)) % 360;
        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        prevAngle = -angle;
        return -angle;
    }


    private boolean isRotate(MotionEvent event){
        int dx1 = (int) (event.getX(0) - event.getX(1));
        int dy1 = (int) (event.getY(0) - event.getY(1));
        int dx2 = (int) (event.getX(1) - event.getX(2));
        int dy2 = (int) (event.getY(1) - event.getY(2));
        Log.d("dx1 ", ""+ dx1);
        Log.d("dx2 ", "" + dx2);
        Log.d("dy1 ", "" + dy1);
        Log.d("dy2 ", "" + dy2);
        //pointer 1
        if(Math.abs(dx1) > Math.abs(dy1) && Math.abs(dx2) > Math.abs(dy2)) {
            if(dx1 >= 2.0 && dx2 <=  -2.0){
                Log.d("first pointer ", "right");
                return true;
            }
            else if(dx1 <= -2.0 && dx2 >= 2.0){
                Log.d("first pointer ", "left");
                return true;
            }
        }
        else {
            if(dy1 >= 2.0 && dy2 <=  -2.0){
                Log.d("seccond pointer ", "top");
                return true;
            }
            else if(dy1 <= -2.0 && dy2 >= 2.0){
                Log.d("second pointer ", "bottom");
                return true;
            }

        }

        return false;
    }


    public interface OnRotationGestureListener {
        void onRotation(RotationGestureDetector rotationDetector, float lastUpdatedPrevAngle);
    }
}