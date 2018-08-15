package com.kippin.bankstatement.adapter;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by gaganpreet.singh on 2/11/2016.
 */
public class ScrollSync2 {

    ListView baseList = null;

    ListView syncList=null;

    public ScrollSync2(ListView baseList,ListView syncList){
        this.baseList = baseList;
        this.syncList = syncList;
    }


    boolean isLeftListEnabled = true;
    boolean isRightListEnabled = true;

    public  void attach(){

        baseList.setOnTouchListener(onTouchListener);
        syncList.setOnTouchListener(onTouchListener);


        baseList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // onScroll will be called and there will be an infinite loop.
                // That's why i set a boolean value
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isRightListEnabled = false;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isRightListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View c = view.getChildAt(0);
                if (c != null && isLeftListEnabled) {
                    syncList.setSelectionFromTop(firstVisibleItem, c.getTop());
                }
            }
        });

        syncList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isLeftListEnabled = false;
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    isLeftListEnabled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                View c = view.getChildAt(0);
                if (c != null && isRightListEnabled) {
                    baseList.setSelectionFromTop(firstVisibleItem, c.getTop());
                }
            }
        });
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            touched = (ListView) v;

            ListView second = null;

            if(touched == baseList){
                second = syncList;
            }else
            {
                second = baseList;
            }

//            View c = touched.getChildAt(0);
//            int scrolly = -c.getTop() + touched.getFirstVisiblePosition() * c.getHeight();



            second.scrollTo(second.getScrollX(),getScroll(touched));

            return false;
        }
    };
    private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<Integer, Integer>();

    private int getScroll(ListView listView) {
        View c = listView.getChildAt(0); //this is the first visible row
        int scrollY = -c.getTop();
        listViewItemHeights.put(listView.getFirstVisiblePosition(), c.getHeight());
        for (int i = 0; i < listView.getFirstVisiblePosition(); ++i) {
            if (listViewItemHeights.get(i) != null) // (this is a sanity check)
                scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone
        }
        return scrollY;
    }
    private ListView touched = null;

}

