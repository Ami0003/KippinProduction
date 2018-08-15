package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by sandeep.singh on 7/19/2016.
 */
abstract public class AnalyticAdapter extends BaseAdapter {
    List<? extends Object> lisData;
    LayoutInflater inflater = null;
    public AnalyticAdapter(){

    }
    public AnalyticAdapter(List<? extends Object> lisData , Activity activity){
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
   abstract public int getCount() ;

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);
}
