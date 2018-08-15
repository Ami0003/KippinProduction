package com.kippinretail.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics.ResponseForPointAnalytics;
import com.kippinretail.R;

import java.util.List;

/**
 * Created by sandeep.singh on 7/19/2016.
 */
public class Analytic_PointAdapter extends BaseAdapter {
    List<ResponseForPointAnalytics> listData;
    Activity mContext ;
    LayoutInflater inflater;

    public Analytic_PointAdapter(List<ResponseForPointAnalytics> listData , Activity activity){
        this.listData = listData;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if(listData!=null){
            return listData.size();
        }
        else{
            return  0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    View lastView;
    int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = (View)inflater.inflate(R.layout.subitem_point_analytics,parent,false);
            holder.txt1 =(TextView) convertView.findViewById(R.id.txt_col1);
            holder.txt2 =(TextView) convertView.findViewById(R.id.txt_col2);
            holder.txt3 =(TextView) convertView.findViewById(R.id.txt_col3);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
            holder.txt1.setText("");
            holder.txt2.setText("");
            holder.txt3.setText("");
        }
        ResponseForPointAnalytics response = listData.get(position);
        holder.txt1.setText(response.getDate());
        holder.txt2.setText(response.getPoints());
        holder.txt3.setText(response.getUsername());

        final View finalView = convertView;
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (lastView != null && lastPosition != position) {
                    lastView.setBackgroundColor(Color.WHITE);
                }

                if (lastPosition != position) {
                    lastPosition = position;
                    lastView = finalView;
                    finalView.setBackgroundColor(Color.LTGRAY);
                }

                return false;
            }
        });


        if(lastPosition ==position){
            convertView.setBackgroundColor(Color.LTGRAY);
        }else if(lastPosition!=position && lastPosition>=0){
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;

    }
    class ViewHolder{
        TextView txt1,txt2,txt3;
    }
}
