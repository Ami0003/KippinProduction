package com.kippin.bankstatement;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gaganpreet.singh on 1/27/2016.
 */

public class ScrollSync implements AbsListView.OnScrollListener,
        View.OnTouchListener, GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;
    private Set<ListView> listSet = new HashSet<ListView>();
    private ListView currentTouchSource;

    private int currentOffset = 0;
    private int currentPosition = 0;

    public void addList(ListView list) {
        listSet.add(list);
        list.setOnTouchListener(this);
        list.setSelectionFromTop(currentPosition, currentOffset);

        if (gestureDetector == null)
            gestureDetector = new GestureDetector(list.getContext(), this);
    }

    public void removeList(ListView list) {
        listSet.remove(list);
    }

    public boolean onTouch(View view, MotionEvent event) {

//        try{
            ListView list = (ListView) view;

            if (currentTouchSource != null) {
                list.setOnScrollListener(null);
                return gestureDetector.onTouchEvent(event);
            } else {
                list.setOnScrollListener(this);
                currentTouchSource = list;

                for (ListView list1 : listSet)
                    if (list1 != currentTouchSource)
                        list1.dispatchTouchEvent(event);

                currentTouchSource = null;
                return false;
            }
//        }catch (Exception e){
//            e.printStackTrace();;
//        }
//        catch (Error e){
//            e.printStackTrace();;
//        }
//        return false;

    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (view.getChildCount() > 0) {
            currentPosition = view.getFirstVisiblePosition();
            currentOffset = view.getChildAt(0).getTop();
        }
    }

    private boolean scrolling;

    public boolean onDown(MotionEvent e) {
        return !scrolling;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        scrolling = scrollState != SCROLL_STATE_IDLE;
    }

    // GestureDetector callbacks
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        return false;
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    public void onLongPress(MotionEvent e) {
    }

    public void onShowPress(MotionEvent e) {
    }
}